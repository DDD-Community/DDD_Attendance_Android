package com.ddd.attendance.feature.admin.attendance.model

data class AttendanceBoardStatus(
    val attendance: Int,
    val late: Int,
    val absent: Int
)