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

/**
 * 운영진 투표 목록 항목.
 */
data class VoteSummary(
    val voteId: Int,
    val title: String,
    val status: VoteStatus,
    val openedAt: String?,
    val closedAt: String?,
    val createdDate: String?
)

/**
 * 운영진 투표 관리 화면의 상태 + 참여 현황.
 */
data class VoteParticipation(
    val voteId: Int,
    val title: String,
    val status: VoteStatus,
    val totalMembers: Int,
    val respondedMembers: Int,
    val participationRate: Int
)

/**
 * 미참여 멤버 명단.
 */
data class VoteNonResponders(
    val totalCount: Int,
    val members: List<NonResponder>
)

data class NonResponder(
    val memberId: Int,
    val name: String,
    val teamName: String?,
    val todayAttendanceStatus: TodayAttendanceStatus
)

enum class TodayAttendanceStatus {
    ATTENDED, LATE, ABSENT, NONE
}
