package com.ddd.attendance.feature.admin.attendance.model

data class MemberAttendanceInfo(
    val name: String,
    val role: String,
    val attendanceType: MemberAttendanceType
)