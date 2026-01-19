package com.ddd.attendance.data.api

import com.ddd.attendance.data.model.ActivityScheduleResponse
import retrofit2.http.GET

interface SchedulesApi {
    @GET("api/schedules")
    suspend fun getSchedules(): List<ActivityScheduleResponse>
}
