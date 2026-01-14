package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.admin.AdminMe
import com.ddd.attendance.domain.repository.AdminMyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAdminMeUseCase @Inject constructor(
    private val adminMyPageRepository: AdminMyPageRepository
) {
    operator fun invoke(): Flow<AdminMe> {
        return adminMyPageRepository.adminMe()
    }
}