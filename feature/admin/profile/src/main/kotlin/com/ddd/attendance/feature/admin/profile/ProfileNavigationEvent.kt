package com.ddd.attendance.feature.admin.profile

sealed class ProfileNavigationEvent {
    object PopBackStack : ProfileNavigationEvent()
}