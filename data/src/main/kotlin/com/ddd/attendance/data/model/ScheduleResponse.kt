package com.ddd.attendance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val id: Long,
    val name: String,
    val status: String,
    val desc: String,
    val month: Int,
    val day: Int
)
