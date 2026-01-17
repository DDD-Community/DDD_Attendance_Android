package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.UsersApi
import com.ddd.attendance.data.api.model.users.UserRequest
import com.ddd.attendance.data.datasource.ApiUsersDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.model.QrResponse
import com.ddd.attendance.data.model.UsersResponse
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
            Log.d(TAG, "=== API USERS REQUEST ===")
            Log.d(TAG, "Name: $name")
            Log.d(TAG, "GenerationId: $generationId")
            Log.d(TAG, "JobRole: $jobRole")
            Log.d(TAG, "TeamId: $teamId")
            Log.d(TAG, "ManagerRoles: $managerRoles")
            Log.d(TAG, "Provider: $provider")
            Log.d(TAG, "Token: ${token.take(20)}...")
            Log.d(TAG, "InvitationCode: $invitationCode")
            Log.d(TAG, "========================")

            userPreferencesDataStore.saveUserRole("MEMBER")

            val response = if (teamId != null) {
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
            } else {
                userPreferencesDataStore.saveUserRole("ADMIN")

                usersApi.users(
                    request = UserRequest(
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

            // 사용자 데이터 저장
            userPreferencesDataStore.saveLoginData(
                userId = response.userId ?: 0,
                name = response.name ?: "",
                email = response.email ?: "",
                generation = response.generation ?: "",
                team = response.team ?: "",
                jobRole = response.jobRole ?: "",
                role = response.role ?: "",
                accessToken = response.accessToken ?: "",
                refreshToken = response.refreshToken ?: ""
            )

            Log.d(TAG, "Users API successful - User ID: ${response.userId}, Name: ${response.name}")
            Log.d(TAG, "Generation: ${response.generation}, Team: ${response.team}, JobRole: ${response.jobRole}, Role: ${response.role}")
            Log.d(TAG, "User data saved to DataStore")

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
            Log.e(TAG, "Users API failed", e)
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
}