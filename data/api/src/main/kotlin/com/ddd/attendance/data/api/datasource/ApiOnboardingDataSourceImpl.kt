package com.ddd.attendance.data.api.datasource

import com.ddd.attendance.data.api.OnboardingApi
import com.ddd.attendance.data.datasource.ApiOnboardingDataSource
import com.ddd.attendance.data.model.JobRoleResponse
import com.ddd.attendance.data.model.TeamResponse
import com.ddd.attendance.data.model.VerifyCodeResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiOnboardingDataSourceImpl @Inject constructor(
    private val onBoardingApi: OnboardingApi
) : ApiOnboardingDataSource {
    override suspend fun verifyCode(code: String): Result<VerifyCodeResponse> {
        return try {
            val data = onBoardingApi.verifyCode(code)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTeams(id: Int): Result<List<TeamResponse>> {
        return try {
            val data = onBoardingApi.getTeams(id)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getJobs(): Result<List<JobRoleResponse>> {
        return try {
            val data = onBoardingApi.getJobs()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRole(): Result<List<JobRoleResponse>> {
        return try {
            val data = onBoardingApi.getRole()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}