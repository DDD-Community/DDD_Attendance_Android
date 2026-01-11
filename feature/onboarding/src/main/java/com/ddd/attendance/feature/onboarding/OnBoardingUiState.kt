package com.ddd.attendance.feature.onboarding

import androidx.compose.runtime.Stable
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
 * 3 → OnBoardingType(팀/업무)에 따라 최종 선택 화면
 * */
@Stable
data class OnBoardingUiState(
    val type: UserType = UserType.Member,
    val step: OnBoardingStep = OnBoardingStep.Invite,
    val index: Int = 0,
    val grayBlockCount: Int = 0,
    val blackBlockCount: Int = 0,
    val inputInvitePinCode: String = "",
    val pinCodeStatus: PinCodeStatus = PinCodeStatus.Idle,
    val generationId: Int = -1,
    val generationName: String = "",
    val name: String = "",
    val title: String = "",
    val subTitle: String = "",
    val selectedItemMap: Map<OnBoardingStep, ImmutableList<SelectItemUiModel>> = emptyMap()
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