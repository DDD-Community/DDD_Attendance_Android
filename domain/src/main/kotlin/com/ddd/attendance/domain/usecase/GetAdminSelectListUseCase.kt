package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.onboarding.ItemSelect
import com.ddd.attendance.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAdminSelectListUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(): Flow<Map<String, List<ItemSelect>>> {
        return onboardingRepository.getAdminSelectList()
    }
}