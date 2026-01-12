package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.onboarding.ItemSelect
import com.ddd.attendance.domain.model.onboarding.VerifyCode
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun verifyCode(code: String): Flow<VerifyCode>
    fun getAdminSelectList(): Flow<Map<String, List<ItemSelect>>>
    fun getMemberSelectList(id: Int): Flow<Map<String, List<ItemSelect>>>
}
