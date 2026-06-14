package com.ddd.attendance.data.api

import com.ddd.attendance.data.model.vote.ActiveVoteResponse
import com.ddd.attendance.data.model.vote.FeedbackTemplateResponse
import com.ddd.attendance.data.model.vote.MyVoteStatusResponse
import com.ddd.attendance.data.model.vote.TeamVoteTemplateResponse
import com.ddd.attendance.data.model.vote.VoteSubmitRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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
}
