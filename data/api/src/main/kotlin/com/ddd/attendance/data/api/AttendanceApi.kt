package com.ddd.attendance.data.api

import com.ddd.attendance.data.api.model.attendance.AttendanceChangeRequest
import com.ddd.attendance.data.api.model.attendance.AttendanceRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AttendanceApi {
    @PUT("api/attendances")
    suspend fun attendancesChange(@Body request: AttendanceChangeRequest): Response<Unit>

    @POST("api/attendances")
    suspend fun attendances(@Body request: AttendanceRequest): Response<Unit>
}