package com.ddd.attendance.data.api.model.users

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val name: String,
    val generationId: Int,
    val jobRole: String,
    val teamId: Int? = null,
    val managerRoles: List<String> = emptyList(),
    val provider: String,
    val token: String,
    val oauthRefreshToken: String,
    val invitationCode: String
)