package com.ddd.attendance.data.model.vote

import kotlinx.serialization.Serializable

@Serializable
data class VoteSummaryResponse(
    val voteId: Int,
    val title: String = "",
    val status: String = "",
    val openedAt: String? = null,
    val closedAt: String? = null,
    val createdDate: String? = null
)

@Serializable
data class VoteParticipationResponse(
    val voteId: Int,
    val title: String = "",
    val status: String = "",
    val totalMembers: Int = 0,
    val respondedMembers: Int = 0,
    val participationRate: Int = 0
)

@Serializable
data class VoteNonRespondersResponse(
    val totalCount: Int = 0,
    val members: List<NonResponderResponse> = emptyList()
)

@Serializable
data class NonResponderResponse(
    val memberId: Int,
    val name: String = "",
    val teamName: String? = null,
    val todayAttendanceStatus: String = "NONE"
)

@Serializable
data class VoteDetailResponse(
    val voteId: Int,
    val title: String = "",
    val status: String = "",
    val templateVersion: Int = 0,
    val teamVoteTemplate: TeamVoteTemplateBody? = null,
    val feedbackTemplate: FeedbackTemplateBody? = null
)

@Serializable
data class TeamVoteResultsResponse(
    val voteId: Int,
    val title: String = "",
    val status: String = "",
    val totalResponses: Int = 0,
    val categories: List<TeamVoteResultCategoryResponse> = emptyList()
)

@Serializable
data class TeamVoteResultCategoryResponse(
    val categoryId: String,
    val title: String = "",
    val order: Int = 0,
    val teams: List<TeamVoteResultTeamResponse> = emptyList(),
    val reasons: List<String> = emptyList()
)

@Serializable
data class TeamVoteResultTeamResponse(
    val rank: Int = 0,
    val teamId: Int,
    val name: String = "",
    val serviceName: String? = null,
    val voteCount: Int = 0
)

@Serializable
data class FeedbackResultsResponse(
    val voteId: Int,
    val totalResponses: Int = 0,
    val questions: List<FeedbackResultQuestionResponse> = emptyList()
)

@Serializable
data class FeedbackResultQuestionResponse(
    val questionId: String,
    val title: String = "",
    val type: String = "",
    val order: Int = 0,
    val options: List<FeedbackResultOptionResponse>? = null,
    val trueCount: Int? = null,
    val falseCount: Int? = null,
    val textAnswers: List<String>? = null
)

@Serializable
data class FeedbackResultOptionResponse(
    val optionId: String,
    val label: String = "",
    val count: Int = 0
)
