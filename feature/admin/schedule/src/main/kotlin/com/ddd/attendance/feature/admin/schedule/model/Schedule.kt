package com.ddd.attendance.feature.admin.schedule.model

data class Schedule(
    val month: String,
    val day: String,
    val title: String,
    val description: String,
    val isSelected: Boolean
)