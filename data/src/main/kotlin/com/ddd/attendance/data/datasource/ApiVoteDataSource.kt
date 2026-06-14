package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.vote.ActiveVoteResponse
import com.ddd.attendance.data.model.vote.FeedbackTemplateResponse
import com.ddd.attendance.data.model.vote.MyVoteStatusResponse
import com.ddd.attendance.data.model.vote.TeamVoteTemplateResponse
import com.ddd.attendance.data.model.vote.VoteSubmitRequest

interface ApiVoteDataSource {
    suspend fun getActiveVote(): Result<ActiveVoteResponse?>
    suspend fun getTeamVoteTemplate(voteId: Int): Result<TeamVoteTemplateResponse>
    suspend fun getFeedbackTemplate(voteId: Int): Result<FeedbackTemplateResponse>
    suspend fun getMyVoteStatus(voteId: Int): Result<MyVoteStatusResponse>
    suspend fun submitVote(voteId: Int, request: VoteSubmitRequest): Result<Unit>
}
