package com.ddd.attendance.feature.onboarding

sealed class NavigationEvent {
    object PopBackStack : NavigationEvent()
    data class GoToDestination(val destination: String) : NavigationEvent()
    data class FailOnBoarding(val message: String): NavigationEvent()
}