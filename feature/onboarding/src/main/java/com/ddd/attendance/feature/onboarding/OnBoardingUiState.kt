package com.ddd.attendance.feature.onboarding

import androidx.compose.runtime.Stable
import com.ddd.attendance.domain.model.onboarding.OnboardingEntryPoint
import com.ddd.attendance.feature.core.model.UserType
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import com.ddd.attendance.feature.onboarding.select.SelectItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * OnBoarding 진행 단계 정의
 * step:
 * 0 → 초대 코드 입력 화면
 * 1 → 사용자 이름 입력 화면
 * 2 → 직무 선택 화면
 * 3 → 운영진: 업무 선택 화면 / 멤버: 팀 선택 화면
 * 4 -> 운영진 : 팀 매니징 선택시 팀 선택 화면
 * */
@Stable
data class OnBoardingUiState(
    // 사용자 타입 관련
    val type: UserType = UserType.MEMBER,
    val onBoardingUserType: OnboardingUserType = OnboardingUserType.MEMBER,
    val entryPoint: OnboardingEntryPoint = OnboardingEntryPoint.SIGN_UP,

    // 단계 관련
    val step: OnBoardingStep = OnBoardingStep.Invite,
    val index: Int = 0,
    val maxStepIndex: Int = 0,
    val stepItems: ImmutableList<OnBoardingStep> = persistentListOf(),
    val grayBlockCount: Int = 0,
    val blackBlockCount: Int = 0,

    // 초대 코드 / 핀코드 관련
    val inputInvitePinCode: String = "",
    val pinCodeStatus: PinCodeStatus = PinCodeStatus.Idle,

    // 사용자 정보
    val name: String = "",
    val generationId: Int = -1,
    val generationName: String = "",
    val title: String = "",
    val subTitle: String = "",

    // 직무 / 팀 / 역할 관련
    val jobRole: String = "",
    val teamId: Int = 0,
    val managerRoles: ImmutableList<String> = persistentListOf(),
    val isTeamManagingSelected: Boolean = false,

    // 선택 아이템 맵
    val selectedItemMap: Map<OnBoardingStep, ImmutableList<SelectItemUiModel>> = emptyMap(),
) {
    val currentSelectItems: ImmutableList<SelectItemUiModel>
        get() = selectedItemMap[step] ?: persistentListOf()

    val canGoNext: Boolean
        get() = when (step) {
            OnBoardingStep.Invite -> pinCodeStatus in listOf(PinCodeStatus.Ready, PinCodeStatus.Success)
            OnBoardingStep.Name -> name.length in 1..5
            OnBoardingStep.Job -> currentSelectItems.any { it.isSelected }
            OnBoardingStep.Team -> currentSelectItems.any { it.isSelected }
            OnBoardingStep.Role -> currentSelectItems.any { it.isSelected }
        }
}