package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Unit> {
        return userRepository.logout()
    }
}