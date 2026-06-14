package com.ddd.attendance.domain.model.vote

/**
 * STEP 2 참여 경험 피드백 템플릿. 질문은 타입별로 동적으로 렌더링된다.
 */
data class FeedbackTemplate(
    val status: VoteStatus,
    val title: String,
    val description: String,
    val questions: List<FeedbackQuestion>
)

enum class FeedbackQuestionType {
    TEAM_SELECT, MULTI_SELECT, LONG_TEXT, BOOLEAN, UNKNOWN
}

data class FeedbackQuestion(
    val id: String,
    val order: Int,
    val type: FeedbackQuestionType,
    val title: String,
    val helpText: String?,
    val required: Boolean,
    val maxSelectableOptions: Int?,
    val maxLength: Int?,
    val options: List<FeedbackOption>,
    val followUp: FeedbackQuestion?
)

data class FeedbackOption(
    val id: String,
    val label: String
)
