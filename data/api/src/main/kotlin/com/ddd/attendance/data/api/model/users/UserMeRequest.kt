package com.ddd.attendance.data.api.model.users

import kotlinx.serialization.Serializable

@Serializable
data class UserMeRequest(
    val name: String,
    val generationId: Int,
    val jobRole: String,
    val teamId: Int? = null,
    val managerRoles: List<String>? = null,
    val invitationCode: String
)