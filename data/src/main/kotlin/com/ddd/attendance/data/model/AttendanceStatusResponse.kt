package com.ddd.attendance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceStatusResponse(
    val name: String,
    val code: String
)