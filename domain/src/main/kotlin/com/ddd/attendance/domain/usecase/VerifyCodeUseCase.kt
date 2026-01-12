package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.onboarding.VerifyCode
import com.ddd.attendance.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyCodeUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(code: String): Flow<VerifyCode> {
        return onboardingRepository.verifyCode(code)
    }
}