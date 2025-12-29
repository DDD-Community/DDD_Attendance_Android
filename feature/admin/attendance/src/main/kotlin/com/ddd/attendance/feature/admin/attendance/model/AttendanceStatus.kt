package com.ddd.attendance.feature.admin.attendance.model

data class AttendanceStatus(
    val attendance: Int,
    val late: Int,
    val absent: Int
)