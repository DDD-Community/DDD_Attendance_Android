package com.ddd.attendance.feature.onboarding.invite

enum class PinCodeStatus {
    Idle,       // 입력중
    Ready,      // 4자리 입력
    Success,    // 요청 성공
    Fail       // 요청 실패
}