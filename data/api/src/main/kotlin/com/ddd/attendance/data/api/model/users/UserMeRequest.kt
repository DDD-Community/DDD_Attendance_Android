package com.ddd.attendance.data.api.model.users

import kotlinx.serialization.Serializable

@Serializable
data class UserMeRequest(
    val name: String,
    val generationId: Int,
    val jobRole: String,
    val teamId: Int,
    val managerRoles: List<String>,
    val invitationCode: String
)