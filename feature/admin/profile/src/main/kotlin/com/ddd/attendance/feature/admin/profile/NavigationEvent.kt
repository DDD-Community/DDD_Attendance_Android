package com.ddd.attendance.feature.admin.profile

sealed class NavigationEvent {
    object PopBackStack : NavigationEvent()
}