package com.ddd.attendance.feature.member.main

sealed class MemberNavigationEvent {
    object GoToLogin: MemberNavigationEvent()
}