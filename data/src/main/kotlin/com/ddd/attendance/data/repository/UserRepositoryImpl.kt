package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datasource.ApiUsersDataSource
import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.mapper.users.toDomain
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.model.users.Users
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val googleLoginDataSource: GoogleLoginDataSource,
    private val apiLoginDataSource: ApiLoginDataSource,
    private val apiUsersDataSource: ApiUsersDataSource,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : UserRepository {
    
    override fun login(loginType: LoginType): Flow<Unit> = flow {
        when (loginType) {
            LoginType.GOOGLE -> {
                val socialLoginResult = googleLoginDataSource.login()
                if (socialLoginResult.isFailure) {
                    throw IllegalStateException("Google login failed")
                }

                val idToken = socialLoginResult.getOrThrow()

                // 온보딩 화면에서 사용할 수 있도록 임시 저장
                userPreferencesDataStore.saveTempOAuthToken(
                    token = idToken,
                    provider = "GOOGLE"
                )

                // TODO: 온보딩 완료 후 API 호출하도록 이동
//                 val apiResult = apiLoginDataSource.login(idToken)
//                 if (apiResult.isFailure) {
//                     throw IllegalStateException("API login failed")
//                 }

                emit(Unit)
            }
        }
    }

    override fun usersSave(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Flow<Users>  = flow {
        val result = apiUsersDataSource.users(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            teamId = teamId,
            managerRoles = managerRoles,
            provider = provider,
            token = token,
            invitationCode = invitationCode
        )
        val response = result.getOrThrow()
        emit(response.toDomain())
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val accessToken = userPreferencesDataStore.accessToken.firstOrNull()
        return !accessToken.isNullOrEmpty()
    }

    override suspend fun getUserRole(): String? {
        return userPreferencesDataStore.oauthProvider.firstOrNull()
    }

    override suspend fun hasTempOAuthToken(): Boolean {
        val tempToken = userPreferencesDataStore.tempOauthToken.firstOrNull()
        return !tempToken.isNullOrEmpty()
    }

    override suspend fun completeOnboardingAndLogin(): Result<Unit> {
        return try {
            // 임시 저장된 OAuth 토큰 가져오기
            val tempToken = userPreferencesDataStore.tempOauthToken.firstOrNull()

            if (tempToken.isNullOrEmpty()) {
                return Result.failure(IllegalStateException("No temp OAuth token found"))
            }

            // API 로그인 호출
            val apiResult = apiLoginDataSource.login(tempToken)

            if (apiResult.isSuccess) {
                // 임시 토큰 삭제
                userPreferencesDataStore.clearTempOAuthData()
                Result.success(Unit)
            } else {
                apiResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}