package com.ddd.attendance.data.repository

import com.ddd.attendance.data.api.UserApi
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    
    override suspend fun login(loginType: LoginType): Result<Unit> {
        return try {
            userApi.login()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}