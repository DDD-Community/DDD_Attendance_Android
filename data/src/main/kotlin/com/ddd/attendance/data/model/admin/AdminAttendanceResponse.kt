package com.ddd.attendance.data.model.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminAttendanceResponse(
    val totalAttended: Int,
    val totalLate: Int,
    val totalAbsent: Int
)