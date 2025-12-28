package com.ddd.attendance.feature.core.model

data class AttendanceUiModel(
    val type: AttendanceStatus,
    val count: Int
)