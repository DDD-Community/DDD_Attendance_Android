package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.AuthenticationApi
import com.ddd.attendance.data.api.model.LoginRequest
import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.model.CodeResult
import com.ddd.attendance.data.model.LoginResponse
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiLoginDataSourceImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ApiLoginDataSource {

    override suspend fun login(idToken: String): Result<CodeResult<LoginResponse>> {
        return try {
            val request = LoginRequest(provider = "GOOGLE", token = idToken)

            Log.d(TAG, "=== API LOGIN REQUEST ===")
            Log.d(TAG, "Provider: ${request.provider}")
            Log.d(TAG, "Token: ${idToken.take(20)}...")
            Log.d(TAG, "========================")

            val response = authenticationApi.login(request)

            val result = response.body()?: return Result.failure(IllegalStateException("Login response body is null"))

            // 로그인 데이터 저장 (generation, team, jobRole, role은 users API에서 받아옴)
            userPreferencesDataStore.saveLoginData(
                userId = result.userId?: 0,
                name = result.name ?: "",
                email = result.email ?: "",
                generation = "",
                team = "",
                jobRole = "",
                role = "",
                accessToken = result.accessToken ?: "",
                refreshToken = result.refreshToken ?: ""
            )

            Log.d(TAG, "Login successful - User ID: ${result.userId}, Name: ${result.name}, Email: ${result.email}")
            Log.d(TAG, "isNewUser: ${result.isNewUser}, Provider: ${result.oauthProvider}")
            Log.d(TAG, "Access Token saved to DataStore")


            Result.success(
                value = CodeResult(
                    code = response.code(),
                    response = response)
            )

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
            Log.e(TAG, "Login failed", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ApiLoginDataSource"
    }
}