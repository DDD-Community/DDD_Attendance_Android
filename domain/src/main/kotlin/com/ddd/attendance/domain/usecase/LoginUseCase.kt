package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.Login
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(loginType: LoginType, isAutoLogin: Boolean): Flow<Login> {
        return userRepository.login(isAutoLogin)
    }
}