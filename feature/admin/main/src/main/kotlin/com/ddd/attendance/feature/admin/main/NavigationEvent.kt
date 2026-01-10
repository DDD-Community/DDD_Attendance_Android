package com.ddd.attendance.feature.admin.main

sealed class NavigationEvent {
    object PopBackStack : NavigationEvent()
    object GoToProfile: NavigationEvent()
}