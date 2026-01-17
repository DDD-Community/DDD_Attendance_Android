package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.CodeResult
import com.ddd.attendance.data.model.LoginResponse

interface ApiLoginDataSource {
    suspend fun login(idToken: String): Result<CodeResult<LoginResponse>>
}