package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.vote.ActiveVote
import com.ddd.attendance.domain.model.vote.FeedbackTemplate
import com.ddd.attendance.domain.model.vote.MyVoteStatus
import com.ddd.attendance.domain.model.vote.TeamVoteTemplate
import com.ddd.attendance.domain.model.vote.VoteSubmission
import kotlinx.coroutines.flow.Flow

interface VoteRepository {
    fun getActiveVote(): Flow<ActiveVote?>
    fun getTeamVoteTemplate(voteId: Int): Flow<TeamVoteTemplate>
    fun getFeedbackTemplate(voteId: Int): Flow<FeedbackTemplate>
    fun getMyVoteStatus(voteId: Int): Flow<MyVoteStatus>
    fun submitVote(voteId: Int, submission: VoteSubmission): Flow<Unit>
}
