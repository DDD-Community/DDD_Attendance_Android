package com.ddd.attendance.feature.onboarding

sealed class OnboardingNavigationEvent {
    object PopBackStack : OnboardingNavigationEvent()
    data class GoToDestination(val destination: String) : OnboardingNavigationEvent()
    data class FailOnBoarding(val message: String): OnboardingNavigationEvent()
}