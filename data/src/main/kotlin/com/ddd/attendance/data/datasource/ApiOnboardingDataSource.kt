package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.JobRoleResponse
import com.ddd.attendance.data.model.TeamResponse
import com.ddd.attendance.data.model.VerifyCodeResponse

interface ApiOnboardingDataSource {
    suspend fun verifyCode(code: String): Result<VerifyCodeResponse>
    suspend fun getTeams(id: Int): Result<List<TeamResponse>>
    suspend fun getJobs(): Result<List<JobRoleResponse>>
    suspend fun getRole(): Result<List<JobRoleResponse>>
}