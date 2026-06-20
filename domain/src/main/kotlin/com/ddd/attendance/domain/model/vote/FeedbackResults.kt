package com.ddd.attendance.domain.model.vote

/**
 * 피드백 결과 집계. 질문 타입별로 형태가 다르다.
 * - MULTI_SELECT: options 사용
 * - BOOLEAN: trueCount / falseCount 사용
 * - LONG_TEXT: textAnswers 사용
 */
data class FeedbackResults(
    val voteId: Int,
    val totalResponses: Int,
    val questions: List<FeedbackResultQuestion>
)

data class FeedbackResultQuestion(
    val questionId: String,
    val title: String,
    val type: FeedbackQuestionType,
    val order: Int,
    val options: List<FeedbackResultOption>,
    val trueCount: Int?,
    val falseCount: Int?,
    val textAnswers: List<String>
)

data class FeedbackResultOption(
    val optionId: String,
    val label: String,
    val count: Int
)
