package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.ActivityScheduleResponse

interface ApiSchedulesDataSource {
    suspend fun getSchedules(): Result<List<ActivityScheduleResponse>>
}
