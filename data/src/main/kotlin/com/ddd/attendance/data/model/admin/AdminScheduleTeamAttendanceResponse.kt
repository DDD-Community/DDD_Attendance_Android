package com.ddd.attendance.data.model.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminScheduleTeamAttendanceResponse(
    val attendanceId: Int?,
    val userId: Int?,
    val userName: String?,
    val userInfo: String?,
    val attendanceStatus: String?
)