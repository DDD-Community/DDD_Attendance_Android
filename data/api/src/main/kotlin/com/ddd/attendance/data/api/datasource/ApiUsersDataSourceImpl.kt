package com.ddd.attendance.data.api.datasource

import com.ddd.attendance.data.api.UsersApi
import com.ddd.attendance.data.api.model.users.UserRequest
import com.ddd.attendance.data.datasource.ApiUsersDataSource
import com.ddd.attendance.data.model.UsersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiUsersDataSourceImpl @Inject constructor(
    private val usersApi: UsersApi
) : ApiUsersDataSource {
    override suspend fun users(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Result<UsersResponse> {
        return try {
            Result.success(
                value =
                    usersApi.users(
                        request = UserRequest(
                            name = name,
                            generationId = generationId,
                            jobRole = jobRole,
                            teamId = teamId,
                            managerRoles = managerRoles,
                            provider = provider,
                            token = token,
                            invitationCode = invitationCode
                        )
                    )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}