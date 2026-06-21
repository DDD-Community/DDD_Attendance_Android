package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.vote.ActiveVoteResponse
import com.ddd.attendance.data.model.vote.FeedbackResultsResponse
import com.ddd.attendance.data.model.vote.FeedbackTemplateResponse
import com.ddd.attendance.data.model.vote.MyVoteStatusResponse
import com.ddd.attendance.data.model.vote.TeamVoteResultsResponse
import com.ddd.attendance.data.model.vote.TeamVoteTemplateResponse
import com.ddd.attendance.data.model.vote.VoteDetailResponse
import com.ddd.attendance.data.model.vote.VoteNonRespondersResponse
import com.ddd.attendance.data.model.vote.VoteParticipationResponse
import com.ddd.attendance.data.model.vote.VoteSubmitRequest
import com.ddd.attendance.data.model.vote.VoteSummaryResponse

interface ApiVoteDataSource {
    suspend fun getActiveVote(): Result<ActiveVoteResponse?>
    suspend fun getTeamVoteTemplate(voteId: Int): Result<TeamVoteTemplateResponse>
    suspend fun getFeedbackTemplate(voteId: Int): Result<FeedbackTemplateResponse>
    suspend fun getMyVoteStatus(voteId: Int): Result<MyVoteStatusResponse>
    suspend fun submitVote(voteId: Int, request: VoteSubmitRequest): Result<Unit>

    suspend fun getVotes(): Result<List<VoteSummaryResponse>>
    suspend fun openVote(voteId: Int): Result<Unit>
    suspend fun closeVote(voteId: Int): Result<Unit>
    suspend fun getVoteParticipation(voteId: Int): Result<VoteParticipationResponse>
    suspend fun getNonResponders(voteId: Int): Result<VoteNonRespondersResponse>
    suspend fun getVoteDetail(voteId: Int): Result<VoteDetailResponse>
    suspend fun getTeamVoteResults(voteId: Int): Result<TeamVoteResultsResponse>
    suspend fun getFeedbackResults(voteId: Int): Result<FeedbackResultsResponse>
}
