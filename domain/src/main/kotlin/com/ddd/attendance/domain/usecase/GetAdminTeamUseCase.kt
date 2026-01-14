package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.admin.AdminTeam
import com.ddd.attendance.domain.repository.AdminMyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAdminTeamUseCase @Inject constructor(
    private val adminMyPageRepository: AdminMyPageRepository
) {
    operator fun invoke(): Flow<List<AdminTeam>> {
        return adminMyPageRepository.adminTeams()
    }
}