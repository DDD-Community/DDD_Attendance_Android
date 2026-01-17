package com.ddd.attendance.data.datasource

interface ApiAttendanceDataSource {
    suspend fun attendancesChange(attendanceId: Long, scheduleId: Long, status: String, userId: Long): Result<Unit>
    suspend fun attendances(qrCode: String): Result<Unit>
}