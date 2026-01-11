package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.LoginType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(loginType: LoginType): Flow<Unit>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getUserRole(): String?
}