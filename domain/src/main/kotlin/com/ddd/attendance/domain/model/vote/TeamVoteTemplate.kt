package com.ddd.attendance.domain.model.vote

/**
 * STEP 1 팀 투표 템플릿. 부문(category)과 평가 대상 팀 목록을 포함한다.
 */
data class TeamVoteTemplate(
    val status: VoteStatus,
    val title: String,
    val description: String,
    val notice: String,
    val categories: List<VoteCategory>,
    val teams: List<VoteTeam>
)

data class VoteCategory(
    val id: String,
    val order: Int,
    val title: String,
    val maxSelectableTeams: Int,
    val reasonRequired: Boolean,
    val reasonMinLength: Int,
    val reasonMaxLength: Int,
    val reasonLabel: String
)

data class VoteTeam(
    val teamId: Int,
    val name: String,
    val serviceName: String?,
    val isOwnTeam: Boolean
)
