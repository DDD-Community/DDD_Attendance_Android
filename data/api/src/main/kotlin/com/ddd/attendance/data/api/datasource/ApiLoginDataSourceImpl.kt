package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.AuthenticationApi
import com.ddd.attendance.data.api.model.LoginRequest
import com.ddd.attendance.data.datasource.ApiLoginDataSource
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiLoginDataSourceImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi
) : ApiLoginDataSource {

    override suspend fun login(idToken: String): Result<Unit> {
        return try {
            val request = LoginRequest(
                provider = "GOOGLE",
                token = idToken
            )

            Log.d(TAG, "=== API LOGIN REQUEST ===")
            Log.d(TAG, "Provider: ${request.provider}")
            Log.d(TAG, "Token: ${idToken.take(20)}...")
            Log.d(TAG, "========================")

            val response = authenticationApi.login(request)

            Log.d(TAG, "Login successful - User ID: ${response.userId}, Name: ${response.name}, Email: ${response.email}")
            Log.d(TAG, "isNewUser: ${response.isNewUser}, Provider: ${response.oauthProvider}")

            Result.success(Unit)
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