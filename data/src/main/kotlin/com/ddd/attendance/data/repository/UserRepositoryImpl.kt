package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val googleLoginDataSource: GoogleLoginDataSource,
    private val apiLoginDataSource: ApiLoginDataSource,
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
                val apiResult = apiLoginDataSource.login(idToken)
                if (apiResult.isFailure) {
                    throw IllegalStateException("API login failed")
                }
                
                emit(Unit)
            }
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val accessToken = userPreferencesDataStore.accessToken.firstOrNull()
        return !accessToken.isNullOrEmpty()
    }

    override suspend fun getUserRole(): String? {
        return userPreferencesDataStore.oauthProvider.firstOrNull()
    }
}