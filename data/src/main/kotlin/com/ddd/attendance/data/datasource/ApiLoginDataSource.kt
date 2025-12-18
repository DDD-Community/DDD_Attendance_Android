package com.ddd.attendance.data.datasource

interface ApiLoginDataSource {
    suspend fun login(idToken: String): Result<Unit>
}