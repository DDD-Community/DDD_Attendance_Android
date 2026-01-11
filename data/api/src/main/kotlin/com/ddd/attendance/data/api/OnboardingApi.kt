package com.ddd.attendance.data.api

import com.ddd.attendance.data.model.JobRoleResponse
import com.ddd.attendance.data.model.TeamResponse
import com.ddd.attendance.data.model.VerifyCodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OnboardingApi {
    @GET("api/onboarding/verify-code")
    suspend fun verifyCode(@Query("code") code: String): VerifyCodeResponse

    @GET("api/onboarding/teams")
    suspend fun getTeams(@Query("generationId") id: Int): List<TeamResponse>

    @GET("api/onboarding/jobs")
    suspend fun getJobs(): List<JobRoleResponse>

    @GET("api/onboarding/manager-roles")
    suspend fun getRole(): List<JobRoleResponse>
}