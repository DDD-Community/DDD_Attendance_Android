package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.onboarding.ItemSelect
import com.ddd.attendance.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemberSelectListUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(id: Int): Flow<Map<String, List<ItemSelect>>> {
        return onboardingRepository.getMemberSelectList(id)
    }
}