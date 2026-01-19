package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.attendance.AttendanceStatus
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun attendancesChange(attendanceId: Long, scheduleId: Long, status: String, userId: Long): Flow<Unit>
    fun attendances(qrCode: String): Flow<Unit>
    fun attendancesStatus(): Flow<List<AttendanceStatus>>
}