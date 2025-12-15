package com.ddd.attendance.feature.onboarding

import androidx.compose.runtime.Stable
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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

    val dummyType: OnBoardingType = OnBoardingType.Member,
    val dummyRoleList: ImmutableList<String> = persistentListOf("Product Manager", "Product Designer", "iOS", "Frontend", "Backend"),
    val dummyTeamList: ImmutableList<String> = persistentListOf("Android 1팀", "Android2팀", "iOS 1팀", "iOS 2팀", "Web 1팀", "Web 2팀"),
    val dummyWorkList: ImmutableList<String> = persistentListOf("팀 매니징", "일정 리마인드", "사진 촬영", "장소 대관", "SNS 관리", "출석 체크"),
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
