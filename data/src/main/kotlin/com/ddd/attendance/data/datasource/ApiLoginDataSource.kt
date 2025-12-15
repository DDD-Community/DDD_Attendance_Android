package com.ddd.attendance.data.datasource

import com.ddd.attendance.domain.model.LoginType

interface ApiLoginDataSource {
    suspend fun login(idToken: String): Result<Unit>
}