package com.ddd.attendance.data.api

import com.ddd.attendance.data.api.model.users.UserRequest
import com.ddd.attendance.data.model.UsersResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApi {
    @POST("/api/users")
    suspend fun users(@Body request: UserRequest): UsersResponse
}

