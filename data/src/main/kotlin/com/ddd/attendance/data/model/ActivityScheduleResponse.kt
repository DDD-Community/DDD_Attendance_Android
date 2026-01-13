package com.ddd.attendance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ActivityScheduleResponse(
    val id: Long,
    val name: String,
    val desc: String,
    val year: Int,
    val month: Int,
    val day: Int
)
