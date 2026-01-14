package com.ddd.attendance.data.model.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminTeamResponse(
    val teamId: Int,
    val name: String
)