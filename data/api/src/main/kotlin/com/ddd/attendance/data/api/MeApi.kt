package com.ddd.attendance.data.api

import com.ddd.attendance.data.api.model.MeResponse
import com.ddd.attendance.data.model.AttendanceResponse
import com.ddd.attendance.data.model.ScheduleResponse
import retrofit2.http.GET

interface MeApi {

    @GET("api/me")
    suspend fun getMe(): MeResponse

    @GET("api/me/schedules")
    suspend fun getSchedules(): List<ScheduleResponse>

    @GET("api/me/attendances")
    suspend fun getAttendances(): AttendanceResponse
}
