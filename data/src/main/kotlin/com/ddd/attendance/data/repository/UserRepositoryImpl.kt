package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val googleLoginDataSource: GoogleLoginDataSource,
    private val apiLoginDataSource: ApiLoginDataSource
) : UserRepository {
    
    override suspend fun login(loginType: LoginType): Result<Unit> {
        return when (loginType) {
            LoginType.GOOGLE -> {
                googleLoginDataSource.login()
                    .mapCatching { idToken ->
                        apiLoginDataSource.login(idToken)
                            .getOrThrow()
                    }
            }
        }
    }
}