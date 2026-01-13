package com.ddd.attendance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceResponse(
    val totalAttended: Int,
    val totalLate: Int,
    val totalAbsent: Int
)
