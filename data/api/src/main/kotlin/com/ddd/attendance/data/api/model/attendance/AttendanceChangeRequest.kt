package com.ddd.attendance.data.api.model.attendance

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceChangeRequest(
    val attendanceId: Long,
    val scheduleId: Long,
    val status: String,
    val userId: Long
)