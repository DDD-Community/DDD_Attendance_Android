package com.ddd.attendance.feature.member.vote

sealed class VoteNavigationEvent {
    object PopBackStack : VoteNavigationEvent()
    data class OnApiErrorMessage(val throwable: Throwable) : VoteNavigationEvent()
}
