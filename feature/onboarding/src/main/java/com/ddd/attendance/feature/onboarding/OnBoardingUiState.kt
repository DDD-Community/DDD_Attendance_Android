package com.ddd.attendance.feature.onboarding

import androidx.compose.runtime.Stable
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
    val type: OnBoardingType = OnBoardingType.Member,
    val step: OnBoardingStep = OnBoardingStep.Invite,
    val index: Int = 0,
    val grayBlockCount: Int = 0,
    val blackBlockCount: Int = 0,
    val inputInvitePinCode: String = "",
    val pinCodeStatus: PinCodeStatus = PinCodeStatus.Idle,
    val name: String = "",
    val title: String = "",
    val subTitle: String = "",
    val dummyType: OnBoardingType = OnBoardingType.Member,
    val dummyList: Map<OnBoardingStep, ImmutableList<SelectItemUiModel>> =
        defaultSelectItemMap(),
) {
    val currentSelectItems: ImmutableList<SelectItemUiModel>
        get() = dummyList[step] ?: persistentListOf()

    val canGoNext: Boolean
        get() = when (step) {
            OnBoardingStep.Invite ->
                pinCodeStatus in listOf(
                    PinCodeStatus.Ready,
                    PinCodeStatus.Success
                )
            OnBoardingStep.Name -> name.length in 1..5
            OnBoardingStep.Role -> currentSelectItems.any { it.isSelected }
            OnBoardingStep.Team -> currentSelectItems.any { it.isSelected }
            OnBoardingStep.Work -> currentSelectItems.any { it.isSelected }
        }
}

private fun defaultSelectItemMap(): Map<OnBoardingStep, ImmutableList<SelectItemUiModel>> =
    mapOf(
        OnBoardingStep.Role to persistentListOf(
            SelectItemUiModel(OnBoardingStep.Role, 1, "Product Manager", "", false),
            SelectItemUiModel(OnBoardingStep.Role, 2, "Product Designer", "", false),
            SelectItemUiModel(OnBoardingStep.Role, 3, "Android", "", false),
            SelectItemUiModel(OnBoardingStep.Role, 4, "iOS", "", false),
            SelectItemUiModel(OnBoardingStep.Role, 5, "Frontend", "", false),
            SelectItemUiModel(OnBoardingStep.Role, 6, "Backend", "", false)
        ),

        OnBoardingStep.Team to persistentListOf(
            SelectItemUiModel(OnBoardingStep.Team, 1, "Android 1팀", "", false),
            SelectItemUiModel(OnBoardingStep.Team, 2, "Android 2팀", "", false),
            SelectItemUiModel(OnBoardingStep.Team, 3, "iOS 1팀", "", false),
            SelectItemUiModel(OnBoardingStep.Team, 4, "iOS 2팀", "", false),
            SelectItemUiModel(OnBoardingStep.Team, 5, "Web 1팀", "", false),
            SelectItemUiModel(OnBoardingStep.Team, 6, "Web 2팀", "", false)
        ),

        OnBoardingStep.Work to persistentListOf(
            SelectItemUiModel(OnBoardingStep.Work, 1, "팀 매니징", "", false),
            SelectItemUiModel(OnBoardingStep.Work, 2, "일정 리마인드", "", false),
            SelectItemUiModel(OnBoardingStep.Work, 3, "사진 촬영", "", false),
            SelectItemUiModel(OnBoardingStep.Work, 4, "장소 대관", "", false),
            SelectItemUiModel(OnBoardingStep.Work, 5, "SNS 관리", "", false),
            SelectItemUiModel(OnBoardingStep.Work, 6, "출석 체크", "", false)
        )
    )