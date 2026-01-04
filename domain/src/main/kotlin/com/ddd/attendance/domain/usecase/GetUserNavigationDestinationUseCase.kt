package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.repository.UserRepository
import javax.inject.Inject

class GetUserNavigationDestinationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): NavigationDestination {
        return when {
            !isUserLoggedIn() -> NavigationDestination.Login
            !isOnboardingCompleted() -> NavigationDestination.OnBoarding
            isManagerRole() -> NavigationDestination.Manager
            else -> NavigationDestination.Member
        }
    }

    private suspend fun isUserLoggedIn(): Boolean {
        // TODO: 사용자 로그인 상태 체크
        // userRepository를 통해 토큰 유효성 확인
        return true // 임시로 true 반환
    }

    private suspend fun isOnboardingCompleted(): Boolean {
        // TODO: 실제 온보딩 상태 체크 로직 구현
        // 예시: SharedPreferences나 데이터베이스에서 온보딩 완료 여부 확인
        // 또는 userRepository를 통해 사용자 프로필 완성도 확인
        return true // 임시로 true 반환
    }

    private suspend fun isManagerRole(): Boolean {
        // TODO: 사용자 권한이 매니저인지 체크
        // userRepository를 통해 사용자 역할 확인
        return false // 임시로 true 반환
    }
}