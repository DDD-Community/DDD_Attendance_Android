package com.ddd.attendance.data.datasource

import com.ddd.attendance.domain.model.LoginType

interface LoginDataSource {
    suspend fun login(loginType: LoginType): Result<String>
}