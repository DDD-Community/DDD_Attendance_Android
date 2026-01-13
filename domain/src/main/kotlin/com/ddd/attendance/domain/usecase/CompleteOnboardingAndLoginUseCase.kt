package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompleteOnboardingAndLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Unit> = flow {
        val result = userRepository.completeOnboardingAndLogin()

        if (result.isSuccess) {
            emit(Unit)
        } else {
            throw result.exceptionOrNull() ?: Exception("Login failed")
        }
    }
}
