package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.AuthenticationApi
import com.ddd.attendance.data.api.model.LoginRequest
import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiLoginDataSourceImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ApiLoginDataSource {

    override suspend fun login(idToken: String): Result<Unit> {
        return try {
            val request = LoginRequest(
                provider = "GOOGLE",
                token = idToken
            )
            val response = authenticationApi.login(request)

            // 로그인 데이터 저장
            userPreferencesDataStore.saveLoginData(
                userId = response.userId?: 0,
                name = response.name?: "",
                email = response.email?: "",
                oauthProvider = response.oauthProvider?: "",
                message = response.message?: "",
                isNewUser = response.isNewUser?: false,
                accessToken = response.accessToken?: "",
                refreshToken = response.refreshToken?: "",
            )

            Log.d(TAG, "Login successful - User ID: ${response.userId}, Name: ${response.name}, Email: ${response.email}")
            Log.d(TAG, "isNewUser: ${response.isNewUser}, Provider: ${response.oauthProvider}")
            Log.d(TAG, "Access Token saved to DataStore")

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