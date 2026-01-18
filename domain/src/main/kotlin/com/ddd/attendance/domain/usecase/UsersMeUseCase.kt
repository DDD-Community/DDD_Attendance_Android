package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.users.UsersMe
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersMeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        managerRoles: List<String>,
        invitationCode: String
    ): Flow<UsersMe> {
        return userRepository.usersMe(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            teamId = teamId,
            managerRoles = managerRoles,
            invitationCode = invitationCode
        )
    }
}