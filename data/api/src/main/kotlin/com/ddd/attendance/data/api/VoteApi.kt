package com.ddd.attendance.data.api

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteApi {
    @GET("api/votes/active")
    suspend fun getActiveVote(): Response<ActiveVoteResponse>

    @GET("api/votes/{voteId}/team-vote/template")
    suspend fun getTeamVoteTemplate(@Path("voteId") voteId: Int): TeamVoteTemplateResponse

    @GET("api/votes/{voteId}/feedback/template")
    suspend fun getFeedbackTemplate(@Path("voteId") voteId: Int): FeedbackTemplateResponse

    @GET("api/votes/{voteId}/responses/me")
    suspend fun getMyVoteStatus(@Path("voteId") voteId: Int): MyVoteStatusResponse

    @POST("api/votes/{voteId}/responses")
    suspend fun submitVote(
        @Path("voteId") voteId: Int,
        @Body request: VoteSubmitRequest
    ): Response<Unit>

    @GET("api/votes")
    suspend fun getVotes(): List<VoteSummaryResponse>

    @PATCH("api/votes/{voteId}/open")
    suspend fun openVote(@Path("voteId") voteId: Int): Response<Unit>

    @PATCH("api/votes/{voteId}/close")
    suspend fun closeVote(@Path("voteId") voteId: Int): Response<Unit>

    @GET("api/votes/{voteId}/participation")
    suspend fun getVoteParticipation(@Path("voteId") voteId: Int): VoteParticipationResponse

    @GET("api/votes/{voteId}/non-responders")
    suspend fun getNonResponders(@Path("voteId") voteId: Int): VoteNonRespondersResponse

    @GET("api/votes/{voteId}")
    suspend fun getVoteDetail(@Path("voteId") voteId: Int): VoteDetailResponse

    @GET("api/votes/{voteId}/team-vote/results")
    suspend fun getTeamVoteResults(@Path("voteId") voteId: Int): TeamVoteResultsResponse

    @GET("api/votes/{voteId}/feedback/results")
    suspend fun getFeedbackResults(@Path("voteId") voteId: Int): FeedbackResultsResponse
}
