package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.repository.UserRepository
import javax.inject.Inject

class DeleteDataStoreWithdrawAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isLogout: Boolean) {
        userRepository.deleteDataStoreWithdrawAccount(isLogout)
    }
}