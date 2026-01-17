package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteDataStoreWithdrawAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(isLogout: Boolean): Flow<Unit> = flow {
        emit(userRepository.deleteDataStoreWithdrawAccount(isLogout))
    }
}