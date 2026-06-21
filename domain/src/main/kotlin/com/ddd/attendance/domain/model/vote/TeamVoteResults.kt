package com.ddd.attendance.domain.model.vote

/**
 * 팀 투표 결과 집계. 부문(category)별로 득표 순위와 작성 사유(익명)를 포함한다.
 */
data class TeamVoteResults(
    val voteId: Int,
    val title: String,
    val status: VoteStatus,
    val totalResponses: Int,
    val categories: List<TeamVoteResultCategory>
)

data class TeamVoteResultCategory(
    val categoryId: String,
    val title: String,
    val order: Int,
    val teams: List<TeamVoteResultTeam>,
    val reasons: List<String>
)

data class TeamVoteResultTeam(
    val rank: Int,
    val teamId: Int,
    val name: String,
    val serviceName: String?,
    val voteCount: Int
)
