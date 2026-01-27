package com.ddd.attendance.data.datasource

interface GoogleLoginDataSource {
    suspend fun logout(): Result<Unit>
}