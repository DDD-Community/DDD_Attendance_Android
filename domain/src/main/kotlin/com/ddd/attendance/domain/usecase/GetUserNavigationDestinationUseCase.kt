package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.repository.UserRepository
import javax.inject.Inject

class GetUserNavigationDestinationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): NavigationDestination {
        return when {
            hasTempOAuthToken() -> NavigationDestination.OnBoarding
            !isUserLoggedIn() -> NavigationDestination.Login
            !isOnboardingCompleted() -> NavigationDestination.OnBoarding
            isManagerRole() -> NavigationDestination.Manager
            else -> NavigationDestination.Member
        }
    }

    private suspend fun hasTempOAuthToken(): Boolean {
        return userRepository.hasTempOAuthToken()
    }

    private suspend fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    private suspend fun isOnboardingCompleted(): Boolean {
        // TODO: 실제 온보딩 상태 체크 로직 구현
        // 현재는 로그인되어 있으면 온보딩 완료로 간주
        return true
    }

    private suspend fun isManagerRole(): Boolean {
        val role = userRepository.getUserRole()
        // TODO: 실제 역할 체크 로직 구현 (API 응답에 role 필드가 추가되면 수정 필요)
        return false
    }
}