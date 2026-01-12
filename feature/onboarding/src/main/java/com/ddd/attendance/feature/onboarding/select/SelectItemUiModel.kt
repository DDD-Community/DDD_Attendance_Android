package com.ddd.attendance.feature.onboarding.select

import com.ddd.attendance.feature.onboarding.OnBoardingStep

data class SelectItemUiModel(
    val step: OnBoardingStep,
    val text: String = "",
    val key: String = "",
    val teamId: Int = 0,
    val isSelected: Boolean = false
)