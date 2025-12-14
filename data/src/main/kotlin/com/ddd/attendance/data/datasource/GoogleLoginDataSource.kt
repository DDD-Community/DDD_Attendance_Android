package com.ddd.attendance.data.datasource

interface GoogleLoginDataSource {
    suspend fun login(): Result<String>
}