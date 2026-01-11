package com.ddd.attendance.data.api

import com.ddd.attendance.data.api.model.LoginRequest
import com.ddd.attendance.data.api.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}