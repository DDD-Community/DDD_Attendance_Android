package com.ddd.attendance.feature.admin.schedule.model

data class ScheduleUiModel(
    val id: Long,
    val month: Int,
    val day: Int,
    val name: String,
    val desc: String,
    val isSelected: Boolean
)