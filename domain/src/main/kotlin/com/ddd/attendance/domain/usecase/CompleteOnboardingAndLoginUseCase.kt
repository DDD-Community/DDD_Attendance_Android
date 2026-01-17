package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.Login
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompleteOnboardingAndLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Login> {
        return userRepository.completeOnboardingAndLogin()
    }
}