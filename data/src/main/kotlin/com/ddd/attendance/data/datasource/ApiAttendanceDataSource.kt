package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.AttendanceStatusResponse

interface ApiAttendanceDataSource {
    suspend fun attendancesChange(attendanceId: Long, scheduleId: Long, status: String, userId: Long): Result<Unit>
    suspend fun attendances(qrCode: String): Result<Unit>
    suspend fun attendancesStatus(): Result<List<AttendanceStatusResponse>>
}