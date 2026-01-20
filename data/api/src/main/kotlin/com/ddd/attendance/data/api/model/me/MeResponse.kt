package com.ddd.attendance.data.api.model.me

import kotlinx.serialization.Serializable

@Serializable
data class MeResponse(
    val userId: Long?,
    val name: String?,
    val email: String?,
    val generation: String?,
    val team: String?,
    val jobRole: String?,
    val role: String?,
    val managerRoles: List<String>? = null
)