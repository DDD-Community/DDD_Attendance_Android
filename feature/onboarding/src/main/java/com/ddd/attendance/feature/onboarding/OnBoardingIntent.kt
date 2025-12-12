package com.ddd.attendance.feature.onboarding

import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus

sealed interface OnBoardingIntent {
    data object BackStepBlock : OnBoardingIntent
    data object NextStepBlock : OnBoardingIntent
    data class InvitePinCodeChanged(val pinCode: String): OnBoardingIntent
    // 네트워크 결과
    data class VerifyPinCodeResult(val result: PinCodeStatus) : OnBoardingIntent
}