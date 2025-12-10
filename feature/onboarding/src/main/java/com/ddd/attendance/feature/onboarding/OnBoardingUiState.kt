package com.ddd.attendance.feature.onboarding

import androidx.compose.runtime.Stable

/**
 * OnBoarding 진행 단계 정의
 * step:
 * 0 → 초대 코드 입력 화면
 * 1 → 사용자 이름 입력 화면
 * 2 → 직무 선택 화면
 * 3 → OnBoardingType(팀/업무)에 따라 최종 선택 화면
 * */
@Stable
data class OnBoardingUiState(
    val type: OnBoardingType = OnBoardingType.Member,
    val step: Int = 0,
    val whiteBlockCount: Int = 0,
    val blackBlockCount: Int = 0,
)