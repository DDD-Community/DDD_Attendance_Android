package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.repository.UserRepository
import javax.inject.Inject

class GetUserNavigationDestinationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(code: Int = 0): NavigationDestination {
        return when(code) {
            201 -> {
                if (userRepository.getUserRole() == "MANAGER") {
                    NavigationDestination.Manager
                } else {
                    NavigationDestination.Member
                }
            }
            202 -> NavigationDestination.OnBoarding

            else -> NavigationDestination.Login
        }
    }
}