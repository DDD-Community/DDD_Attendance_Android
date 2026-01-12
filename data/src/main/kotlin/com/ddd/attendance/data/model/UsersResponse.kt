package com.ddd.attendance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val userId: Int,
    val name: String,
    val email: String,
    val generation: String,
    val team: String,
    val jobRole: String,
    val role: String,
    val managerRoles: List<String>
)