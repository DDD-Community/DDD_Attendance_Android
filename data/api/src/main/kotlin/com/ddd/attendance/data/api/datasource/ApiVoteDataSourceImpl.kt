package com.ddd.attendance.data.api.datasource

import com.ddd.attendance.data.api.VoteApi
import com.ddd.attendance.data.datasource.ApiVoteDataSource
import com.ddd.attendance.data.mapper.toDomainException
import com.ddd.attendance.data.model.vote.ActiveVoteResponse
import com.ddd.attendance.data.model.vote.FeedbackTemplateResponse
import com.ddd.attendance.data.model.vote.MyVoteStatusResponse
import com.ddd.attendance.data.model.vote.TeamVoteTemplateResponse
import com.ddd.attendance.data.model.vote.VoteSubmitRequest
import retrofit2.HttpException
import javax.inject.Inject

class ApiVoteDataSourceImpl @Inject constructor(
    private val voteApi: VoteApi
) : ApiVoteDataSource {

    override suspend fun getActiveVote(): Result<ActiveVoteResponse?> {
        return try {
            val response = voteApi.getActiveVote()
            if (!response.isSuccessful) throw HttpException(response)
            // 진행 중인 투표가 없으면 204/빈 바디 → null
            Result.success(response.body())
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTeamVoteTemplate(voteId: Int): Result<TeamVoteTemplateResponse> {
        return try {
            Result.success(voteApi.getTeamVoteTemplate(voteId))
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFeedbackTemplate(voteId: Int): Result<FeedbackTemplateResponse> {
        return try {
            Result.success(voteApi.getFeedbackTemplate(voteId))
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyVoteStatus(voteId: Int): Result<MyVoteStatusResponse> {
        return try {
            Result.success(voteApi.getMyVoteStatus(voteId))
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitVote(voteId: Int, request: VoteSubmitRequest): Result<Unit> {
        return try {
            val response = voteApi.submitVote(voteId, request)
            if (!response.isSuccessful) throw HttpException(response)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ApiVoteDataSource"
    }
}
