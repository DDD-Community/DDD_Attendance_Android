package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.UsersResponse

interface ApiUsersDataSource {
    suspend fun users(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        managerRoles: List<String>,
        provider: String,
        token: String,
        oauthRefreshToken: String,
        invitationCode: String
    ): Result<UsersResponse>
}