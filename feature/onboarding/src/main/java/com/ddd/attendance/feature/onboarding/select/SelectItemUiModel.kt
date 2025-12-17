package com.ddd.attendance.feature.onboarding.select

import com.ddd.attendance.feature.onboarding.OnBoardingStep

data class SelectItemUiModel(
    val step: OnBoardingStep,
    val id: Int,
    val text: String,
    val imageUrl: String,
    val isSelected: Boolean
)