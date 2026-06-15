package com.ddd.attendance.domain.model.vote

/**
 * 팀 투표 + 피드백을 한 번에 제출하는 요청 모델.
 */
data class VoteSubmission(
    val teamVote: List<TeamVoteAnswer>,
    val feedback: List<FeedbackAnswer>
)

data class TeamVoteAnswer(
    val categoryId: String,
    val teamIds: List<Int>,
    val reason: String?
)

data class FeedbackAnswer(
    val questionId: String,
    val optionIds: List<String>?,
    val textValue: String?,
    val boolValue: Boolean?
)
