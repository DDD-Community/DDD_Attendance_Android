package com.ddd.attendance.data.model.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminMeResponse(
    val userId: Int?,
    val name: String?,
    val email: String?,
    val generation: String?,
    val team: String?,
    val jobRole: String?,
    val role: String?,
    val managerRoles: List<String>?
)