package com.ddd.attendance.feature.admin.vote

import com.ddd.attendance.feature.admin.vote.model.FeedbackResultsUi
import com.ddd.attendance.feature.admin.vote.model.TeamVoteResultsUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailUi
import com.ddd.attendance.feature.admin.vote.model.VoteListItemUi
import com.ddd.attendance.feature.admin.vote.model.VoteMember
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class VoteUiState(
    val isLoading: Boolean = false,
    val voteId: Int? = null,
    val voteStatus: VoteStatus = VoteStatus.BEFORE,
    val totalMembers: Int = 0,
    val participatedMembers: Int = 0,
    val participationRate: Int = 0,
    val notParticipatedMembers: ImmutableList<VoteMember> = persistentListOf(),
    val isShowStartConfirmDialog: Boolean = false,
    val isShowEndConfirmDialog: Boolean = false,
    val isShowNotParticipatedDialog: Boolean = false,

    val subScreen: VoteSubScreen = VoteSubScreen.Manage,

    val voteList: ImmutableList<VoteListItemUi> = persistentListOf(),
    val isVoteListLoading: Boolean = false,

    val detail: VoteDetailUi? = null,
    val isDetailLoading: Boolean = false,

    val teamResults: TeamVoteResultsUi? = null,
    val feedbackResults: FeedbackResultsUi? = null,
    val isResultsLoading: Boolean = false,
    val resultsTab: VoteResultsTab = VoteResultsTab.TeamVote
)

enum class VoteStatus {
    BEFORE, IN_PROGRESS, CLOSED
}

enum class VoteSubScreen {
    Manage, List, Detail, Results
}

enum class VoteResultsTab {
    TeamVote, Feedback
}
