package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.ScheduleResponse

interface ApiMeDataSource {
    suspend fun getMe(): Result<Unit>
    suspend fun getSchedules(): Result<List<ScheduleResponse>>
}
