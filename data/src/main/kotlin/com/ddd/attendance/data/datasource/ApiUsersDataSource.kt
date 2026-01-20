package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.QrResponse
import com.ddd.attendance.data.model.user.UsersMeResponse
import com.ddd.attendance.data.model.user.UsersResponse

interface ApiUsersDataSource {
    suspend fun users(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Result<UsersResponse>

    suspend fun usersMe(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        invitationCode: String
    ): Result<UsersMeResponse>

    suspend fun getQr(userId: Long): Result<QrResponse>

    suspend fun deleteUsersMe(token: String): Result<Unit>
}