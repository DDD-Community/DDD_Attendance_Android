package com.ddd.attendance.feature.onboarding

import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus

sealed interface OnBoardingIntent {
    data object GoToPreviousStep: OnBoardingIntent
    data object GoToNextStep: OnBoardingIntent
    data class InvitePinCodeChanged(val pinCode: String): OnBoardingIntent
    data class VerifyPinCodeResult(val result: PinCodeStatus): OnBoardingIntent
    data class NameChanged(val name: String): OnBoardingIntent
    data class SelectListItem(val position: Int): OnBoardingIntent
}
