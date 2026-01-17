package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiAttendanceDataSource
import com.ddd.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val apiAttendanceDataSource: ApiAttendanceDataSource
): AttendanceRepository {
    override fun attendancesChange(
        attendanceId: Long,
        scheduleId: Long,
        status: String,
        userId: Long
    ): Flow<Unit> = flow {
        apiAttendanceDataSource.attendancesChange(
            attendanceId = attendanceId,
            scheduleId = scheduleId,
            status = status,
            userId = userId
        )
            .onFailure { throw it }
            .onSuccess { emit(Unit) }
    }

    override fun attendances(qrCode: String): Flow<Unit> = flow {
        apiAttendanceDataSource.attendances(qrCode = qrCode)
            .onFailure { throw it }
            .onSuccess { emit(Unit) }
    }
}