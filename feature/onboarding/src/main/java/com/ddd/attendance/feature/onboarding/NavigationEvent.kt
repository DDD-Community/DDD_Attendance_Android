package com.ddd.attendance.feature.onboarding

sealed class NavigationEvent {
    object PopBackStack : NavigationEvent()
    object GoToHome : NavigationEvent()
}