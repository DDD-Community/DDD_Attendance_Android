package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.attendance.AttendanceStatus
import com.ddd.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceStatusUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(): Flow<List<AttendanceStatus>> {
        return attendanceRepository.attendancesStatus()
    }
}