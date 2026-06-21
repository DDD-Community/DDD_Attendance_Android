package com.ddd.attendance.feature.admin.vote

sealed class VoteNavigationEvent {
    data class OnApiErrorMessage(val throwable: Throwable) : VoteNavigationEvent()
}
