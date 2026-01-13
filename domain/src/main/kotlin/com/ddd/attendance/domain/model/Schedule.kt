package com.ddd.attendance.domain.model

data class Schedule(
    val id: Long,
    val name: String,
    val status: String,
    val desc: String,
    val month: Int,
    val day: Int
)
