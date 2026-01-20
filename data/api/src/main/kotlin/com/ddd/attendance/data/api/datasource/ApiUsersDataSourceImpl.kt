package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.UsersApi
import com.ddd.attendance.data.api.model.users.UserMeRequest
import com.ddd.attendance.data.api.model.users.UserRequest
import com.ddd.attendance.data.datasource.ApiUsersDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.mapper.toDomainException
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
    ): Result<UsersResponse> {
        return try {
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

            val response = if (teamId == null || teamId == 0) {
                usersApi.users(
                    UserRequest(
                        managerRoles = managerRoles,
                        name = name,
                        generationId = generationId,
                        jobRole = jobRole,
                        provider = provider,
                        token = token,
                        invitationCode = invitationCode
                    )
                )
            } else {
                usersApi.users(
                    UserRequest(
                        teamId = teamId,
                        managerRoles = managerRoles,
                        name = name,
                        generationId = generationId,
                        jobRole = jobRole,
                        provider = provider,
                        token = token,
                        invitationCode = invitationCode
                    )
                )
            }

            Log.d(TAG, "Users API success - id=${response.userId}, name=${response.name}")

            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun usersMe(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        invitationCode: String
    ): Result<UsersMeResponse> {
        return try {
            val response = if (teamId == null || teamId == 0) {
                usersApi.usersMe(
                    UserMeRequest(
                        name = name,
                        generationId = generationId,
                        jobRole = jobRole,
                        managerRoles = managerRoles,
                        invitationCode = invitationCode
                    )
                )
            } else {
                usersApi.usersMe(
                    UserMeRequest(
                        name = name,
                        generationId = generationId,
                        jobRole = jobRole,
                        teamId = teamId,
                        managerRoles = managerRoles,
                        invitationCode = invitationCode
                    )
                )
            }

            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Result.failure(e)
        }
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
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Log.e(TAG, "Get QR failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteUsersMe(token: String): Result<Unit> {
        return try {
            usersApi.deleteUsersMe(token = token)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Log.e(TAG, "DeleteUsersMe failed", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ApiUsersDataSource"
    }
}