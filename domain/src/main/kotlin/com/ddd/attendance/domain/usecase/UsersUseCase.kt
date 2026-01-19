package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.users.Users
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Flow<Users> {
        return userRepository.users(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            teamId = teamId,
            managerRoles = managerRoles,
            provider = provider,
            token = token,
            invitationCode = invitationCode
        )
    }
}