package com.ddd.attendance.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UsersMeResponse(
    val userId: Int?,
    val name: String?,
    val email: String?,
    val generation: String?,
    val team: String?,
    val jobRole: String?,
    val role: String?
)