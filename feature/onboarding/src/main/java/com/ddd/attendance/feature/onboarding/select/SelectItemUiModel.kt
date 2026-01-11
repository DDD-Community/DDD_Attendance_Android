package com.ddd.attendance.feature.onboarding.select

import com.ddd.attendance.feature.onboarding.OnBoardingStep

data class SelectItemUiModel(
    val step: OnBoardingStep,
    val text: String,
    val isSelected: Boolean
)