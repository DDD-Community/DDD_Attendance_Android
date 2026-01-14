package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.admin.AdminScheduleTeamAttendance
import com.ddd.attendance.domain.repository.AdminMyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAdminScheduleTeamAttendanceUseCase @Inject constructor(
    private val adminMyPageRepository: AdminMyPageRepository
) {
    operator fun invoke(scheduleId: Int, teamId: Int): Flow<List<AdminScheduleTeamAttendance>> {
        return adminMyPageRepository.adminScheduleTeamAttendances(scheduleId, teamId)
    }
}