package com.ddd.attendance.feature.onboarding

sealed class NavigationEvent {
    object PopBackStack : NavigationEvent()
    object GoToHome : NavigationEvent()
    data class FailOnBoarding(val message: String): NavigationEvent()
}