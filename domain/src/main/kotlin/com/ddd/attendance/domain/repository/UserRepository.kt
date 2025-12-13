package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.LoginType

interface UserRepository {
    suspend fun login(loginType: LoginType): Result<Unit>
}