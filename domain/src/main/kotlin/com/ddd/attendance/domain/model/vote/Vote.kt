package com.ddd.attendance.domain.model.vote

enum class VoteStatus {
    DRAFT, OPEN, CLOSED, UNKNOWN
}

/**
 * 멤버 기수에 진행 중인 투표 정보. 진행 중인 투표가 없으면 null.
 */
data class ActiveVote(
    val voteId: Int,
    val title: String,
    val alreadyResponded: Boolean
)

/**
 * 현재 멤버의 특정 투표 참여 여부.
 */
data class MyVoteStatus(
    val voteId: Int,
    val responded: Boolean
)
