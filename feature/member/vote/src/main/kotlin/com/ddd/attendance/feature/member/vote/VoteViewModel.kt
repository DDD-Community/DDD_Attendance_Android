package com.ddd.attendance.feature.member.vote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.vote.FeedbackAnswer
import com.ddd.attendance.domain.model.vote.FeedbackQuestionType
import com.ddd.attendance.domain.model.vote.FeedbackTemplate
import com.ddd.attendance.domain.model.vote.TeamVoteAnswer
import com.ddd.attendance.domain.model.vote.TeamVoteTemplate
import com.ddd.attendance.domain.model.vote.VoteSubmission
import com.ddd.attendance.domain.usecase.GetActiveVoteUseCase
import com.ddd.attendance.domain.usecase.GetFeedbackTemplateUseCase
import com.ddd.attendance.domain.usecase.GetTeamVoteTemplateUseCase
import com.ddd.attendance.domain.usecase.SubmitVoteUseCase
import com.ddd.attendance.feature.member.vote.model.CategoryUiModel
import com.ddd.attendance.feature.member.vote.model.FeedbackQuestionUiModel
import com.ddd.attendance.feature.member.vote.model.OptionUiModel
import com.ddd.attendance.feature.member.vote.model.TeamUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val getActiveVoteUseCase: GetActiveVoteUseCase,
    private val getTeamVoteTemplateUseCase: GetTeamVoteTemplateUseCase,
    private val getFeedbackTemplateUseCase: GetFeedbackTemplateUseCase,
    private val submitVoteUseCase: SubmitVoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoteUiState())
    val uiState: StateFlow<VoteUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<VoteNavigationEvent>()
    val navigationEvent: SharedFlow<VoteNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        load()
    }

    private fun load() {
        _uiState.update { it.copy(step = VoteStep.LOADING) }
        viewModelScope.launch {
            getActiveVoteUseCase()
                .onEach { active ->
                    when {
                        active == null ->
                            _uiState.update { it.copy(step = VoteStep.ERROR) }
                        active.alreadyResponded ->
                            _uiState.update { it.copy(voteId = active.voteId, step = VoteStep.COMPLETE) }
                        else -> loadTemplates(active.voteId)
                    }
                }
                .catch {
                    _uiState.update { it.copy(step = VoteStep.ERROR) }
                    _navigationEvent.emit(VoteNavigationEvent.OnApiErrorMessage(it))
                }
                .collect()
        }
    }

    private fun loadTemplates(voteId: Int) {
        viewModelScope.launch {
            combine(
                getTeamVoteTemplateUseCase(voteId),
                getFeedbackTemplateUseCase(voteId)
            ) { team, feedback -> team to feedback }
                .onEach { (team, feedback) ->
                    _uiState.update { state ->
                        state.copy(
                            voteId = voteId,
                            step = VoteStep.STEP1,
                            teamVoteTitle = team.title,
                            teamVoteDescription = team.description,
                            teamVoteNotice = team.notice,
                            categories = team.toCategoryUiModels(),
                            teams = team.toTeamUiModels(),
                            feedbackTitle = feedback.title,
                            feedbackDescription = feedback.description,
                            questions = feedback.toQuestionUiModels()
                        )
                    }
                }
                .catch {
                    _uiState.update { it.copy(step = VoteStep.ERROR) }
                    _navigationEvent.emit(VoteNavigationEvent.OnApiErrorMessage(it))
                }
                .collect()
        }
    }

    fun onIntent(intent: VoteIntent) {
        when (intent) {
            is VoteIntent.Submit -> submit()
            is VoteIntent.Retry -> load()
            is VoteIntent.ConfirmExit -> {
                _uiState.update { it.copy(isShowExitDialog = false) }
                viewModelScope.launch { _navigationEvent.emit(VoteNavigationEvent.PopBackStack) }
            }
            else -> _uiState.update { reduce(it, intent) }
        }
    }

    private fun submit() {
        val state = _uiState.value
        if (!state.isStep1Valid || !state.isStep2Valid) return

        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            submitVoteUseCase(state.voteId, state.toSubmission())
                .onEach {
                    _uiState.update { s -> s.copy(isSubmitting = false, step = VoteStep.COMPLETE) }
                }
                .catch {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    _navigationEvent.emit(VoteNavigationEvent.OnApiErrorMessage(it))
                }
                .collect()
        }
    }

    private fun reduce(state: VoteUiState, intent: VoteIntent): VoteUiState {
        return when (intent) {
            is VoteIntent.TeamToggled -> {
                val category = state.categories.firstOrNull { it.id == intent.categoryId }
                    ?: return state
                val team = state.teams.firstOrNull { it.teamId == intent.teamId } ?: return state
                if (team.isOwnTeam) return state
                val current = state.teamSelections[intent.categoryId].orEmpty()
                val updated = when {
                    intent.teamId in current -> current - intent.teamId
                    current.size < category.maxSelectableTeams -> current + intent.teamId
                    else -> return state.copy(
                        toastMessage = "최대 ${category.maxSelectableTeams}팀까지 선택할 수 있어요"
                    )
                }
                state.copy(teamSelections = state.teamSelections + (intent.categoryId to updated))
            }

            is VoteIntent.ReasonChanged -> {
                val category = state.categories.firstOrNull { it.id == intent.categoryId }
                    ?: return state
                val (text, overflow) = clamp(intent.text, category.reasonMaxLength)
                state.copy(
                    reasons = state.reasons + (intent.categoryId to text),
                    toastMessage = if (overflow) overflowMessage(category.reasonMaxLength) else state.toastMessage
                )
            }

            is VoteIntent.GoToStep2 ->
                if (state.isStep1Valid) state.copy(step = VoteStep.STEP2) else state

            is VoteIntent.OptionToggled -> {
                val question = state.findQuestion(intent.questionId) ?: return state
                val current = state.optionAnswers[intent.questionId].orEmpty()
                val max = question.maxSelectableOptions
                val updated = when {
                    intent.optionId in current -> current - intent.optionId
                    max == null || current.size < max -> current + intent.optionId
                    else -> return state.copy(toastMessage = "최대 ${max}개까지 선택할 수 있어요")
                }
                state.copy(optionAnswers = state.optionAnswers + (intent.questionId to updated))
            }

            is VoteIntent.TextChanged -> {
                val max = state.findQuestion(intent.questionId)?.maxLength ?: DEFAULT_TEXT_MAX
                val (text, overflow) = clamp(intent.text, max)
                state.copy(
                    textAnswers = state.textAnswers + (intent.questionId to text),
                    toastMessage = if (overflow) overflowMessage(max) else state.toastMessage
                )
            }

            is VoteIntent.BoolSelected ->
                state.copy(boolAnswers = state.boolAnswers + (intent.questionId to intent.value))

            is VoteIntent.BackPressed -> when (state.step) {
                VoteStep.STEP2 -> state.copy(step = VoteStep.STEP1)
                else -> state.copy(isShowExitDialog = true)
            }
            is VoteIntent.DismissExit -> state.copy(isShowExitDialog = false)
            is VoteIntent.ToastShown -> state.copy(toastMessage = null)

            is VoteIntent.Submit,
            is VoteIntent.Retry,
            is VoteIntent.ConfirmExit -> state
        }
    }

    private fun clamp(text: String, max: Int): Pair<String, Boolean> =
        if (text.length > max) text.take(max) to true else text to false

    private fun overflowMessage(max: Int) = "최대 ${max}자까지 입력할 수 있어요"

    private fun VoteUiState.toSubmission(): VoteSubmission {
        val teamVote = categories.map { category ->
            TeamVoteAnswer(
                categoryId = category.id,
                teamIds = teamSelections[category.id].orEmpty().toList(),
                reason = reasons[category.id]?.takeIf { it.isNotBlank() }
            )
        }
        val feedback = buildList {
            questions.forEach { question -> collectAnswers(question, this@toSubmission, this) }
        }
        return VoteSubmission(teamVote = teamVote, feedback = feedback)
    }

    private fun collectAnswers(
        question: FeedbackQuestionUiModel,
        state: VoteUiState,
        out: MutableList<FeedbackAnswer>
    ) {
        val answer = when (question.type) {
            FeedbackQuestionType.MULTI_SELECT -> state.optionAnswers[question.id].orEmpty()
                .takeIf { it.isNotEmpty() }
                ?.let { FeedbackAnswer(question.id, optionIds = it.toList(), textValue = null, boolValue = null) }

            FeedbackQuestionType.LONG_TEXT -> state.textAnswers[question.id]?.takeIf { it.isNotBlank() }
                ?.let { FeedbackAnswer(question.id, optionIds = null, textValue = it, boolValue = null) }

            FeedbackQuestionType.BOOLEAN -> state.boolAnswers[question.id]
                ?.let { FeedbackAnswer(question.id, optionIds = null, textValue = null, boolValue = it) }

            else -> null
        }
        if (answer != null) out.add(answer)
        question.followUp?.let { collectAnswers(it, state, out) }
    }

    private fun VoteUiState.findQuestion(id: String): FeedbackQuestionUiModel? {
        questions.forEach { q ->
            if (q.id == id) return q
            if (q.followUp?.id == id) return q.followUp
        }
        return null
    }

    // ----- 도메인 → UI 모델 매핑 -----

    private fun TeamVoteTemplate.toCategoryUiModels(): List<CategoryUiModel> =
        categories.map {
            CategoryUiModel(
                id = it.id,
                title = it.title,
                maxSelectableTeams = it.maxSelectableTeams,
                reasonLabel = it.reasonLabel,
                reasonRequired = it.reasonRequired,
                reasonMinLength = it.reasonMinLength,
                reasonMaxLength = it.reasonMaxLength
            )
        }

    private fun TeamVoteTemplate.toTeamUiModels(): List<TeamUiModel> =
        teams.map { TeamUiModel(it.teamId, it.name, it.serviceName, it.isOwnTeam) }

    private fun FeedbackTemplate.toQuestionUiModels(): List<FeedbackQuestionUiModel> =
        questions.map { it.toUiModel() }

    private fun com.ddd.attendance.domain.model.vote.FeedbackQuestion.toUiModel(): FeedbackQuestionUiModel =
        FeedbackQuestionUiModel(
            id = id,
            type = type,
            title = title,
            helpText = helpText,
            required = required,
            maxSelectableOptions = maxSelectableOptions,
            maxLength = maxLength,
            options = options.map { OptionUiModel(it.id, it.label) },
            followUp = followUp?.toUiModel()
        )

    companion object {
        private const val DEFAULT_TEXT_MAX = 300
    }
}
