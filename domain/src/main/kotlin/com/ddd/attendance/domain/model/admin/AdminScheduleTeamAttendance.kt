package com.ddd.attendance.domain.model.admin

data class AdminScheduleTeamAttendance(
    val attendanceId: Int,
    val userId: Int,
    val userName: String,
    val userInfo: String,
    val attendanceStatus: String
)