package com.ddd.attendance.data.api

import retrofit2.http.POST

interface UserApi {
    
    @POST("auth/login")
    suspend fun login()
}