package com.ddd.attendance.feature.admin.vote

sealed interface VoteIntent {
    data object Refresh : VoteIntent
    data object StartVoteClicked : VoteIntent
    data object ConfirmStartVote : VoteIntent
    data object DismissStartConfirmDialog : VoteIntent
    data object EndVoteClicked : VoteIntent
    data object ConfirmEndVote : VoteIntent
    data object DismissEndConfirmDialog : VoteIntent
    data object ShowNotParticipatedDialog : VoteIntent
    data object DismissNotParticipatedDialog : VoteIntent

    data object OpenVoteList : VoteIntent
    data class OpenVoteDetail(val voteId: Int) : VoteIntent
    data class OpenResults(val voteId: Int) : VoteIntent
    data class SelectResultsTab(val tab: VoteResultsTab) : VoteIntent
    data object BackToManage : VoteIntent
    data object BackToList : VoteIntent
}
