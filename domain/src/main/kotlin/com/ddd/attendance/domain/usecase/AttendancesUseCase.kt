package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendancesUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(qrCode: String): Flow<Unit> {
        return attendanceRepository.attendances(qrCode = qrCode)
    }
}