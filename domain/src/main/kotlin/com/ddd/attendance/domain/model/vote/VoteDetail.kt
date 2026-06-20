package com.ddd.attendance.domain.model.vote

/**
 * 운영진 투표 상세. 상태 + 양쪽 템플릿(없을 수 있음).
 */
data class VoteDetail(
    val voteId: Int,
    val title: String,
    val status: VoteStatus,
    val templateVersion: Int,
    val teamVoteTemplate: VoteDetailTeamTemplate?,
    val feedbackTemplate: VoteDetailFeedbackTemplate?
)

data class VoteDetailTeamTemplate(
    val title: String,
    val description: String,
    val notice: String,
    val categories: List<VoteCategory>
)

data class VoteDetailFeedbackTemplate(
    val title: String,
    val description: String,
    val questions: List<FeedbackQuestion>
)
