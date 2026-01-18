package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.UsersApi
import com.ddd.attendance.data.api.model.users.UserMeRequest
import com.ddd.attendance.data.api.model.users.UserRequest
import com.ddd.attendance.data.datasource.ApiUsersDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.model.QrResponse
import com.ddd.attendance.data.model.user.UsersMeResponse
import com.ddd.attendance.data.model.user.UsersResponse
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiUsersDataSourceImpl @Inject constructor(
    private val usersApi: UsersApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ApiUsersDataSource {
    override suspend fun users(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): UsersResponse {

        Log.d(
            TAG,
            """
                === API USERS REQUEST ===
                Name: $name
                GenerationId: $generationId
                JobRole: $jobRole
                TeamId: $teamId
                ManagerRoles: $managerRoles
                Provider: $provider
                Token: ${token.take(20)}...
                InvitationCode: $invitationCode
                ========================
                """.trimIndent()
        )

        saveUserRole(teamId)

        val response = if (teamId != null) {
            usersApi.users(
                createMemberRequest(
                    name = name,
                    generationId = generationId,
                    jobRole = jobRole,
                    teamId = teamId,
                    provider = provider,
                    token = token,
                    invitationCode = invitationCode
                )
            )
        } else {
            usersApi.users(
                createAdminRequest(
                    name = name,
                    generationId = generationId,
                    jobRole = jobRole,
                    managerRoles = managerRoles,
                    provider = provider,
                    token = token,
                    invitationCode = invitationCode
                )
            )
        }

        Log.d(TAG, "Users API success - id=${response.userId}, name=${response.name}")

        return response
    }

    override suspend fun usersMe(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        managerRoles: List<String>,
        invitationCode: String
    ):UsersMeResponse {
        return usersApi.usersMe(
            request = UserMeRequest(
                name = name,
                generationId = generationId,
                jobRole = jobRole,
                teamId = teamId,
                managerRoles = managerRoles,
                invitationCode = invitationCode
            )
        )
    }

    override suspend fun getQr(userId: Long): Result<QrResponse> {
        return try {
            Log.d(TAG, "=== API GET QR REQUEST ===")
            Log.d(TAG, "User ID: $userId")

            val response = usersApi.getQr(userId)

            // QR 데이터 저장
            userPreferencesDataStore.saveQrData(
                qrId = response.id,
                qrBase64 = response.qrBase64
            )

            Log.d(TAG, "Get QR successful - QR ID: ${response.id}")
            Log.d(TAG, "QR data saved to DataStore")

            Result.success(response)
        } catch (e: HttpException) {
            val errorCode = e.code()
            val errorBody = e.response()?.errorBody()?.string()

            Log.e(TAG, "=== HTTP ERROR ===")
            Log.e(TAG, "Status Code: $errorCode")
            Log.e(TAG, "Error Body: $errorBody")
            Log.e(TAG, "Message: ${e.message}")
            Log.e(TAG, "==================")

            Result.failure(Exception("HTTP $errorCode: $errorBody"))
        } catch (e: Exception) {
            Log.e(TAG, "Get QR failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteUsersMe(token: String): Result<Unit> {
        return try {
            usersApi.deleteUsersMe(token = token)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "DeleteUsersMe failed", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ApiUsersDataSource"
    }

    private fun createMemberRequest(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        provider: String,
        token: String,
        invitationCode: String
    ): UserRequest {
        return UserRequest(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            teamId = teamId,
            provider = provider,
            token = token,
            invitationCode = invitationCode
        )
    }

    private fun createAdminRequest(
        name: String,
        generationId: Int,
        jobRole: String,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): UserRequest {
        return UserRequest(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            managerRoles = managerRoles,
            provider = provider,
            token = token,
            invitationCode = invitationCode
        )
    }

    private suspend fun saveUserRole(teamId: Int?) {
        val role = if (teamId == null) "ADMIN" else "MEMBER"
        userPreferencesDataStore.saveUserRole(role)
    }
}