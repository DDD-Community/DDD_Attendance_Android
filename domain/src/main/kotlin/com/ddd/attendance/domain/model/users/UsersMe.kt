package com.ddd.attendance.domain.model.users

data class UsersMe(
    val userId: Int,
    val name: String,
    val email: String,
    val generation: String,
    val team: String,
    val jobRole: String,
    val role: String
)