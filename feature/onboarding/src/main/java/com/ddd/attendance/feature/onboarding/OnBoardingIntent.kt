package com.ddd.attendance.feature.onboarding

sealed interface OnBoardingIntent {
    data object BackStepBlock : OnBoardingIntent
    data object NextStepBlock : OnBoardingIntent
}