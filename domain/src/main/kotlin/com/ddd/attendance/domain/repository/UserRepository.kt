package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.LoginType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(loginType: LoginType): Flow<Boolean>
}