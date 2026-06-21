package com.ddd.attendance.feature.admin.vote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.vote.FeedbackQuestionType
import com.ddd.attendance.domain.model.vote.FeedbackResultQuestion
import com.ddd.attendance.domain.model.vote.FeedbackResults
import com.ddd.attendance.domain.model.vote.NonResponder
import com.ddd.attendance.domain.model.vote.TeamVoteResultCategory
import com.ddd.attendance.domain.model.vote.TeamVoteResultTeam
import com.ddd.attendance.domain.model.vote.TeamVoteResults
import com.ddd.attendance.domain.model.vote.TodayAttendanceStatus
import com.ddd.attendance.domain.model.vote.VoteDetail
import com.ddd.attendance.domain.model.vote.VoteParticipation
import com.ddd.attendance.domain.model.vote.VoteSummary
import com.ddd.attendance.domain.usecase.CloseVoteUseCase
import com.ddd.attendance.domain.usecase.GetFeedbackResultsUseCase
import com.ddd.attendance.domain.usecase.GetNonRespondersUseCase
import com.ddd.attendance.domain.usecase.GetTeamVoteResultsUseCase
import com.ddd.attendance.domain.usecase.GetVoteDetailUseCase
import com.ddd.attendance.domain.usecase.GetVoteParticipationUseCase
import com.ddd.attendance.domain.usecase.GetVotesUseCase
import com.ddd.attendance.domain.usecase.OpenVoteUseCase
import com.ddd.attendance.feature.admin.vote.model.FeedbackResultOptionUi
import com.ddd.attendance.feature.admin.vote.model.FeedbackResultQuestionUi
import com.ddd.attendance.feature.admin.vote.model.FeedbackResultsUi
import com.ddd.attendance.feature.admin.vote.model.TeamVoteResultCategoryUi
import com.ddd.attendance.feature.admin.vote.model.TeamVoteResultTeamUi
import com.ddd.attendance.feature.admin.vote.model.TeamVoteResultsUi
import com.ddd.attendance.feature.admin.vote.model.VoteAttendanceStatus
import com.ddd.attendance.feature.admin.vote.model.VoteDetailCategoryUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailFeedbackTemplateUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailQuestionUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailTeamTemplateUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailUi
import com.ddd.attendance.feature.admin.vote.model.VoteListItemUi
import com.ddd.attendance.feature.admin.vote.model.VoteMember
import com.ddd.attendance.feature.admin.vote.model.VoteStatusKind
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ddd.attendance.domain.model.vote.VoteStatus as DomainVoteStatus

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val getVotesUseCase: GetVotesUseCase,
    private val openVoteUseCase: OpenVoteUseCase,
    private val closeVoteUseCase: CloseVoteUseCase,
    private val getVoteParticipationUseCase: GetVoteParticipationUseCase,
    private val getNonRespondersUseCase: GetNonRespondersUseCase,
    private val getVoteDetailUseCase: GetVoteDetailUseCase,
    private val getTeamVoteResultsUseCase: GetTeamVoteResultsUseCase,
    private val getFeedbackResultsUseCase: GetFeedbackResultsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoteUiState())
    val uiState: StateFlow<VoteUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<VoteNavigationEvent>()
    val navigationEvent: SharedFlow<VoteNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadCurrentVote()
    }

    fun onIntent(intent: VoteIntent) {
        when (intent) {
            is VoteIntent.Refresh -> loadCurrentVote()
            is VoteIntent.StartVoteClicked -> _uiState.update { it.copy(isShowStartConfirmDialog = true) }
            is VoteIntent.DismissStartConfirmDialog -> _uiState.update { it.copy(isShowStartConfirmDialog = false) }
            is VoteIntent.ConfirmStartVote -> confirmStartVote()
            is VoteIntent.EndVoteClicked -> _uiState.update { it.copy(isShowEndConfirmDialog = true) }
            is VoteIntent.DismissEndConfirmDialog -> _uiState.update { it.copy(isShowEndConfirmDialog = false) }
            is VoteIntent.ConfirmEndVote -> confirmEndVote()
            is VoteIntent.ShowNotParticipatedDialog -> loadNonResponders()
            is VoteIntent.DismissNotParticipatedDialog -> _uiState.update {
                it.copy(isShowNotParticipatedDialog = false)
            }
            is VoteIntent.OpenVoteList -> openVoteList()
            is VoteIntent.OpenVoteDetail -> openVoteDetail(intent.voteId)
            is VoteIntent.OpenResults -> openResults(intent.voteId)
            is VoteIntent.SelectResultsTab -> _uiState.update { it.copy(resultsTab = intent.tab) }
            is VoteIntent.BackToManage -> _uiState.update {
                it.copy(subScreen = VoteSubScreen.Manage)
            }
            is VoteIntent.BackToList -> _uiState.update {
                it.copy(subScreen = VoteSubScreen.List, detail = null)
            }
        }
    }

    private fun loadCurrentVote() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getVotesUseCase()
                .onEach { votes ->
                    val current = votes.firstOrNull()
                    if (current == null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                voteId = null,
                                voteStatus = VoteStatus.BEFORE
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                voteId = current.voteId,
                                voteStatus = current.status.toUi()
                            )
                        }
                        if (current.status != DomainVoteStatus.DRAFT) {
                            fetchParticipation(current.voteId)
                        } else {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
                .catch { emitError(it) }
                .collect()
        }
    }

    private fun fetchParticipation(voteId: Int) {
        viewModelScope.launch {
            getVoteParticipationUseCase(voteId)
                .onEach { participation -> applyParticipation(participation) }
                .catch { emitError(it) }
                .collect()
        }
    }

    private fun applyParticipation(participation: VoteParticipation) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voteStatus = participation.status.toUi(),
                totalMembers = participation.totalMembers,
                participatedMembers = participation.respondedMembers,
                participationRate = participation.participationRate
            )
        }
    }

    private fun confirmStartVote() {
        val voteId = _uiState.value.voteId
        if (voteId == null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isShowStartConfirmDialog = false) }
                emitError(IllegalStateException(NO_VOTE_MESSAGE))
            }
            return
        }

        Log.d(TAG, "confirmStartVote 진입 → openVote($voteId) 요청 시작")
        viewModelScope.launch {
            openVoteUseCase(voteId)
                .onEach {
                    Log.d(TAG, "openVote($voteId) 성공")

                    _uiState.update {
                        it.copy(
                            isShowStartConfirmDialog = false,
                            voteStatus = VoteStatus.IN_PROGRESS
                        )
                    }
                    fetchParticipation(voteId)
                }
                .catch {

                    Log.e(TAG, "openVote($voteId) 실패", it)

                    _uiState.update { state -> state.copy(isShowStartConfirmDialog = false) }
                    emitError(it)
                }
                .collect()
        }
    }

    private fun confirmEndVote() {
        val voteId = _uiState.value.voteId
        if (voteId == null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isShowEndConfirmDialog = false) }
                emitError(IllegalStateException(NO_VOTE_MESSAGE))
            }
            return
        }
        viewModelScope.launch {
            closeVoteUseCase(voteId)
                .onEach {
                    _uiState.update {
                        it.copy(
                            isShowEndConfirmDialog = false,
                            voteStatus = VoteStatus.CLOSED
                        )
                    }
                    fetchParticipation(voteId)
                }
                .catch {
                    _uiState.update { state -> state.copy(isShowEndConfirmDialog = false) }
                    emitError(it)
                }
                .collect()
        }
    }

    private fun loadNonResponders() {
        val voteId = _uiState.value.voteId ?: return
        viewModelScope.launch {
            getNonRespondersUseCase(voteId)
                .onEach { result ->
                    _uiState.update {
                        it.copy(
                            notParticipatedMembers = result.members.map { m -> m.toUi() }.toPersistentList(),
                            isShowNotParticipatedDialog = true
                        )
                    }
                }
                .catch { emitError(it) }
                .collect()
        }
    }

    private fun openVoteList() {
        _uiState.update {
            it.copy(
                subScreen = VoteSubScreen.List,
                isVoteListLoading = true
            )
        }
        viewModelScope.launch {
            getVotesUseCase()
                .onEach { votes ->
                    _uiState.update {
                        it.copy(
                            isVoteListLoading = false,
                            voteList = votes.map { v -> v.toListItem() }.toPersistentList()
                        )
                    }
                }
                .catch {
                    _uiState.update { state -> state.copy(isVoteListLoading = false) }
                    emitError(it)
                }
                .collect()
        }
    }

    private fun openVoteDetail(voteId: Int) {
        _uiState.update {
            it.copy(
                subScreen = VoteSubScreen.Detail,
                isDetailLoading = true,
                detail = null
            )
        }
        viewModelScope.launch {
            getVoteDetailUseCase(voteId)
                .onEach { detail ->
                    _uiState.update {
                        it.copy(
                            isDetailLoading = false,
                            detail = detail.toUi()
                        )
                    }
                }
                .catch {
                    _uiState.update { state -> state.copy(isDetailLoading = false) }
                    emitError(it)
                }
                .collect()
        }
    }

    private fun openResults(voteId: Int) {
        _uiState.update {
            it.copy(
                subScreen = VoteSubScreen.Results,
                isResultsLoading = true,
                teamResults = null,
                feedbackResults = null,
                resultsTab = VoteResultsTab.TeamVote
            )
        }
        viewModelScope.launch {
            getTeamVoteResultsUseCase(voteId)
                .onEach { results ->
                    _uiState.update { it.copy(teamResults = results.toUi()) }
                }
                .catch { emitError(it) }
                .collect()
        }
        viewModelScope.launch {
            getFeedbackResultsUseCase(voteId)
                .onEach { results ->
                    _uiState.update {
                        it.copy(
                            isResultsLoading = false,
                            feedbackResults = results.toUi()
                        )
                    }
                }
                .catch {
                    _uiState.update { state -> state.copy(isResultsLoading = false) }
                    emitError(it)
                }
                .collect()
        }
    }

    private suspend fun emitError(throwable: Throwable) {
        _uiState.update { it.copy(isLoading = false) }
        _navigationEvent.emit(VoteNavigationEvent.OnApiErrorMessage(throwable))
    }

    private fun DomainVoteStatus.toUi(): VoteStatus = when (this) {
        DomainVoteStatus.DRAFT, DomainVoteStatus.UNKNOWN -> VoteStatus.BEFORE
        DomainVoteStatus.OPEN -> VoteStatus.IN_PROGRESS
        DomainVoteStatus.CLOSED -> VoteStatus.CLOSED
    }

    private fun NonResponder.toUi(): VoteMember = VoteMember(
        name = name,
        team = teamName.orEmpty(),
        attendanceStatus = todayAttendanceStatus.toUi()
    )

    private fun TodayAttendanceStatus.toUi(): VoteAttendanceStatus = when (this) {
        TodayAttendanceStatus.ATTENDED -> VoteAttendanceStatus.ATTENDED
        TodayAttendanceStatus.LATE -> VoteAttendanceStatus.LATE
        TodayAttendanceStatus.ABSENT -> VoteAttendanceStatus.ABSENT
        TodayAttendanceStatus.NONE -> VoteAttendanceStatus.NONE
    }

    private fun VoteSummary.toListItem(): VoteListItemUi = VoteListItemUi(
        voteId = voteId,
        title = title,
        statusLabel = status.toLabel(),
        statusKind = status.toKind(),
        createdDate = createdDate.orEmpty()
    )

    private fun DomainVoteStatus.toLabel(): String = when (this) {
        DomainVoteStatus.DRAFT -> "투표 전"
        DomainVoteStatus.OPEN -> "진행 중"
        DomainVoteStatus.CLOSED -> "투표 종료"
        DomainVoteStatus.UNKNOWN -> "알 수 없음"
    }

    private fun DomainVoteStatus.toKind(): VoteStatusKind = when (this) {
        DomainVoteStatus.DRAFT -> VoteStatusKind.DRAFT
        DomainVoteStatus.OPEN -> VoteStatusKind.OPEN
        DomainVoteStatus.CLOSED -> VoteStatusKind.CLOSED
        DomainVoteStatus.UNKNOWN -> VoteStatusKind.UNKNOWN
    }

    private fun VoteDetail.toUi(): VoteDetailUi = VoteDetailUi(
        voteId = voteId,
        title = title,
        statusLabel = status.toLabel(),
        statusKind = status.toKind(),
        teamTemplate = teamVoteTemplate?.let { template ->
            VoteDetailTeamTemplateUi(
                title = template.title,
                description = template.description,
                notice = template.notice,
                categories = template.categories.map { category ->
                    VoteDetailCategoryUi(
                        id = category.id,
                        title = category.title,
                        maxSelectableTeams = category.maxSelectableTeams,
                        reasonRequired = category.reasonRequired
                    )
                }
            )
        },
        feedbackTemplate = feedbackTemplate?.let { template ->
            VoteDetailFeedbackTemplateUi(
                title = template.title,
                description = template.description,
                questions = template.questions.map { question ->
                    VoteDetailQuestionUi(
                        id = question.id,
                        type = question.type.toLabel(),
                        title = question.title,
                        required = question.required,
                        options = question.options.map { it.label }
                    )
                }
            )
        }
    )

    private fun FeedbackQuestionType.toLabel(): String = when (this) {
        FeedbackQuestionType.TEAM_SELECT -> "팀 선택"
        FeedbackQuestionType.MULTI_SELECT -> "다중 선택"
        FeedbackQuestionType.LONG_TEXT -> "주관식"
        FeedbackQuestionType.BOOLEAN -> "예/아니오"
        FeedbackQuestionType.UNKNOWN -> "기타"
    }

    private fun TeamVoteResults.toUi(): TeamVoteResultsUi = TeamVoteResultsUi(
        totalResponses = totalResponses,
        categories = categories.map { it.toUi() }
    )

    private fun TeamVoteResultCategory.toUi(): TeamVoteResultCategoryUi =
        TeamVoteResultCategoryUi(
            categoryId = categoryId,
            title = title,
            teams = teams.map { it.toUi() },
            reasons = reasons
        )

    private fun TeamVoteResultTeam.toUi(): TeamVoteResultTeamUi = TeamVoteResultTeamUi(
        rank = rank,
        name = name,
        serviceName = serviceName,
        voteCount = voteCount
    )

    private fun FeedbackResults.toUi(): FeedbackResultsUi = FeedbackResultsUi(
        totalResponses = totalResponses,
        questions = questions.map { it.toUi() }
    )

    private fun FeedbackResultQuestion.toUi(): FeedbackResultQuestionUi = when (type) {
        FeedbackQuestionType.MULTI_SELECT, FeedbackQuestionType.TEAM_SELECT ->
            FeedbackResultQuestionUi.MultiSelect(
                questionId = questionId,
                title = title,
                options = options.map { FeedbackResultOptionUi(it.optionId, it.label, it.count) }
            )
        FeedbackQuestionType.BOOLEAN -> FeedbackResultQuestionUi.Boolean(
            questionId = questionId,
            title = title,
            trueCount = trueCount ?: 0,
            falseCount = falseCount ?: 0
        )
        FeedbackQuestionType.LONG_TEXT -> FeedbackResultQuestionUi.LongText(
            questionId = questionId,
            title = title,
            answers = textAnswers
        )
        FeedbackQuestionType.UNKNOWN -> FeedbackResultQuestionUi.Unknown(
            questionId = questionId,
            title = title
        )
    }

    companion object {
        private const val TAG = "VoteViewModel"
        private const val NO_VOTE_MESSAGE = "아직 만들어진 투표가 없어요. 먼저 투표를 생성해주세요."
    }
}
