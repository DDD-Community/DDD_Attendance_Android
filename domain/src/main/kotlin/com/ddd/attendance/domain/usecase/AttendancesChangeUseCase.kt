package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendancesChangeUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(attendanceId: Long, scheduleId: Long, status: String, userId: Long): Flow<Unit> {
        return attendanceRepository.attendancesChange(
            attendanceId = attendanceId,
            scheduleId = scheduleId,
            status = status,
            userId = userId
        )
    }
}