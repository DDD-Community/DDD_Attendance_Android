package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.admin.AdminAttendance
import com.ddd.attendance.domain.repository.AdminMyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAdminScheduleAttendanceUseCase @Inject constructor(
    private val adminMyPageRepository: AdminMyPageRepository
) {
    operator fun invoke(scheduleId: Int): Flow<AdminAttendance> {
        return adminMyPageRepository.adminAttendances(scheduleId)
    }
}