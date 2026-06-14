package com.ddd.attendance.data.model.vote

import kotlinx.serialization.Serializable

@Serializable
data class VoteSubmitRequest(
    val teamVote: List<TeamVoteCategoryAnswerRequest>,
    val feedback: List<FeedbackQuestionAnswerRequest>
)

@Serializable
data class TeamVoteCategoryAnswerRequest(
    val categoryId: String,
    val teamIds: List<Int>,
    val reason: String? = null
)

@Serializable
data class FeedbackQuestionAnswerRequest(
    val questionId: String,
    val optionIds: List<String>? = null,
    val textValue: String? = null,
    val boolValue: Boolean? = null
)
