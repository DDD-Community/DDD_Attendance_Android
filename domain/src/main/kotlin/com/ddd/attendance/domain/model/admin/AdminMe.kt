package com.ddd.attendance.domain.model.admin

data class AdminMe(
    val userId: Int,
    val name: String,
    val email: String,
    val generation: String,
    val team: String,
    val jobRole: String,
    val role: String,
    val managerRoles: List<String>
)