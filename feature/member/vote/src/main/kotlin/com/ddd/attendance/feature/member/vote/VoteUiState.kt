package com.ddd.attendance.feature.member.vote

import com.ddd.attendance.feature.member.vote.model.CategoryUiModel
import com.ddd.attendance.feature.member.vote.model.FeedbackQuestionUiModel
import com.ddd.attendance.feature.member.vote.model.TeamUiModel

enum class VoteStep {
    LOADING, STEP1, STEP2, COMPLETE, ERROR
}

data class VoteUiState(
    val step: VoteStep = VoteStep.LOADING,
    val voteId: Int = 0,

    // STEP 1 (팀 투표) 템플릿
    val teamVoteTitle: String = "",
    val teamVoteDescription: String = "",
    val teamVoteNotice: String = "",
    val categories: List<CategoryUiModel> = emptyList(),
    val teams: List<TeamUiModel> = emptyList(),
    // STEP 1 응답 — categoryId 기준
    val teamSelections: Map<String, Set<Int>> = emptyMap(),
    val reasons: Map<String, String> = emptyMap(),

    // STEP 2 (피드백) 템플릿
    val feedbackTitle: String = "",
    val feedbackDescription: String = "",
    val questions: List<FeedbackQuestionUiModel> = emptyList(),
    // STEP 2 응답 — questionId 기준 (followUp 도 자체 id 로 저장)
    val optionAnswers: Map<String, Set<String>> = emptyMap(),
    val textAnswers: Map<String, String> = emptyMap(),
    val boolAnswers: Map<String, Boolean> = emptyMap(),

    // 공통
    val isShowExitDialog: Boolean = false,
    val isSubmitting: Boolean = false,
    val toastMessage: String? = null
) {
    /** 부문에 1팀 이상 선택했고, 사유 조건을 만족하면 유효 */
    fun isCategoryValid(category: CategoryUiModel): Boolean {
        val selected = teamSelections[category.id].orEmpty()
        if (selected.isEmpty()) return false
        val reason = reasons[category.id].orEmpty()
        return if (category.reasonRequired || category.reasonMinLength > 0) {
            reason.length in category.reasonMinLength..category.reasonMaxLength
        } else {
            true
        }
    }

    val isStep1Valid: Boolean
        get() = categories.isNotEmpty() && categories.all { isCategoryValid(it) }

    /** 필수 질문에 모두 응답하면 제출 가능 */
    fun isQuestionAnswered(question: FeedbackQuestionUiModel): Boolean {
        if (!question.required) return true
        val answered = when (question.type) {
            com.ddd.attendance.domain.model.vote.FeedbackQuestionType.MULTI_SELECT ->
                optionAnswers[question.id].orEmpty().isNotEmpty()
            com.ddd.attendance.domain.model.vote.FeedbackQuestionType.BOOLEAN ->
                boolAnswers[question.id] != null
            com.ddd.attendance.domain.model.vote.FeedbackQuestionType.LONG_TEXT ->
                textAnswers[question.id].orEmpty().isNotBlank()
            else -> true
        }
        val followUpAnswered = question.followUp?.let { isQuestionAnswered(it) } ?: true
        return answered && followUpAnswered
    }

    val isStep2Valid: Boolean
        get() = questions.all { isQuestionAnswered(it) }
}
