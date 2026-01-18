package com.ddd.attendance.data.api

import com.ddd.attendance.data.api.model.users.UserMeRequest
import com.ddd.attendance.data.api.model.users.UserRequest
import com.ddd.attendance.data.model.QrResponse
import com.ddd.attendance.data.model.user.UsersMeResponse
import com.ddd.attendance.data.model.user.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    @POST("api/users")
    suspend fun users(@Body request: UserRequest): UsersResponse

    @GET("api/users/{id}/qr")
    suspend fun getQr(@Path("id") userId: Long): QrResponse

    @DELETE("api/users/me")
    suspend fun deleteUsersMe(@Query("token") token: String): Response<Unit>

    @PUT("api/users/me")
    suspend fun usersMe(@Body request: UserMeRequest): UsersMeResponse
}

