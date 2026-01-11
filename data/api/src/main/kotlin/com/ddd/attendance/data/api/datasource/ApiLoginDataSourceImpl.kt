package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.AuthenticationApi
import com.ddd.attendance.data.api.model.LoginRequest
import com.ddd.attendance.data.datasource.ApiLoginDataSource
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
            val response = authenticationApi.login(request)

            Log.d(TAG, "Login successful - User ID: ${response.userId}, Name: ${response.name}, Email: ${response.email}")
            Log.d(TAG, "isNewUser: ${response.isNewUser}, Provider: ${response.oauthProvider}")
            Log.d(TAG, "Access Token: ${response.accessToken}")
            // TODO: 토큰 저장 로직 추가 필요

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Login failed", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ApiLoginDataSource"
    }
}