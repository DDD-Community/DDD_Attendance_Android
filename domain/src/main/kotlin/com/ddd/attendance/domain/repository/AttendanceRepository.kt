package com.ddd.attendance.domain.repository

import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun attendancesChange(attendanceId: Long, scheduleId: Long, status: String, userId: Long): Flow<Unit>
    fun attendances(qrCode: String): Flow<Unit>
}