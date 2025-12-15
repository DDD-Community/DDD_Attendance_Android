package com.ddd.attendance.feature.onboarding

import androidx.compose.runtime.Stable
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus

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
    val step: OnBoardingStep = OnBoardingStep.Invite,
    val index: Int = 0,
    val grayBlockCount: Int = 0,
    val blackBlockCount: Int = 0,
    val invitePinCode: String = "",
    val pinCodeStatus: PinCodeStatus = PinCodeStatus.Idle,
    val name: String = "",
) {
    val canGoNext: Boolean
        get() = when (step) {
            OnBoardingStep.Invite ->
                pinCodeStatus in listOf(
                    PinCodeStatus.Ready,
                    PinCodeStatus.Success
                )
            OnBoardingStep.Name -> name.length in 1..5
            else -> true
        }
}
