package com.ddd.attendance.feature.admin.main

sealed class AdminNavigationEvent {
    object PopBackStack : AdminNavigationEvent()
    object GoToProfile: AdminNavigationEvent()
    object GoToLogin: AdminNavigationEvent()
    data class OnQrApiErrorMessage(val throwable: Throwable): AdminNavigationEvent()
}