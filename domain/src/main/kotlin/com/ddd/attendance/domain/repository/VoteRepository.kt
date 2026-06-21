package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.vote.ActiveVote
import com.ddd.attendance.domain.model.vote.FeedbackResults
import com.ddd.attendance.domain.model.vote.FeedbackTemplate
import com.ddd.attendance.domain.model.vote.MyVoteStatus
import com.ddd.attendance.domain.model.vote.TeamVoteResults
import com.ddd.attendance.domain.model.vote.TeamVoteTemplate
import com.ddd.attendance.domain.model.vote.VoteDetail
import com.ddd.attendance.domain.model.vote.VoteNonResponders
import com.ddd.attendance.domain.model.vote.VoteParticipation
import com.ddd.attendance.domain.model.vote.VoteSubmission
import com.ddd.attendance.domain.model.vote.VoteSummary
import kotlinx.coroutines.flow.Flow

interface VoteRepository {
    fun getActiveVote(): Flow<ActiveVote?>
    fun getTeamVoteTemplate(voteId: Int): Flow<TeamVoteTemplate>
    fun getFeedbackTemplate(voteId: Int): Flow<FeedbackTemplate>
    fun getMyVoteStatus(voteId: Int): Flow<MyVoteStatus>
    fun submitVote(voteId: Int, submission: VoteSubmission): Flow<Unit>

    fun getVotes(): Flow<List<VoteSummary>>
    fun openVote(voteId: Int): Flow<Unit>
    fun closeVote(voteId: Int): Flow<Unit>
    fun getVoteParticipation(voteId: Int): Flow<VoteParticipation>
    fun getNonResponders(voteId: Int): Flow<VoteNonResponders>
    fun getVoteDetail(voteId: Int): Flow<VoteDetail>
    fun getTeamVoteResults(voteId: Int): Flow<TeamVoteResults>
    fun getFeedbackResults(voteId: Int): Flow<FeedbackResults>
}
