package com.ddd.attendance.data.model.vote

import kotlinx.serialization.Serializable

@Serializable
data class ActiveVoteResponse(
    val voteId: Int? = null,
    val title: String? = null,
    val alreadyResponded: Boolean = false
)

@Serializable
data class MyVoteStatusResponse(
    val voteId: Int,
    val responded: Boolean = false
)

@Serializable
data class TeamVoteTemplateResponse(
    val templateVersion: Int = 0,
    val status: String = "",
    val template: TeamVoteTemplateBody,
    val teams: List<TeamItemResponse> = emptyList()
)

@Serializable
data class TeamVoteTemplateBody(
    val title: String = "",
    val description: String = "",
    val notice: String = "",
    val categories: List<CategoryResponse> = emptyList()
)

@Serializable
data class CategoryResponse(
    val id: String,
    val order: Int = 0,
    val title: String = "",
    val maxSelectableTeams: Int = 0,
    val reasonRequired: Boolean = false,
    val reasonMinLength: Int = 0,
    val reasonMaxLength: Int = 0,
    val reasonLabel: String = ""
)

@Serializable
data class TeamItemResponse(
    val teamId: Int,
    val name: String = "",
    val serviceName: String? = null,
    val isOwnTeam: Boolean = false
)

@Serializable
data class FeedbackTemplateResponse(
    val templateVersion: Int = 0,
    val status: String = "",
    val template: FeedbackTemplateBody
)

@Serializable
data class FeedbackTemplateBody(
    val title: String = "",
    val description: String = "",
    val questions: List<QuestionResponse> = emptyList()
)

@Serializable
data class QuestionResponse(
    val id: String,
    val order: Int = 0,
    val type: String = "",
    val title: String = "",
    val helpText: String? = null,
    val required: Boolean = false,
    val maxSelectableOptions: Int? = null,
    val maxLength: Int? = null,
    val options: List<OptionResponse>? = null,
    val followUp: QuestionResponse? = null
)

@Serializable
data class OptionResponse(
    val id: String,
    val label: String = ""
)
