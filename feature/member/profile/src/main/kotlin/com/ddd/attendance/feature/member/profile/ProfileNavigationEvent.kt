package com.ddd.attendance.feature.member.profile

sealed class ProfileNavigationEvent {
    object PopBackStack : ProfileNavigationEvent()
    object GoToLogin: ProfileNavigationEvent()
    object GoToOnboarding: ProfileNavigationEvent()
    data class ShowError(val message: String): ProfileNavigationEvent()
}