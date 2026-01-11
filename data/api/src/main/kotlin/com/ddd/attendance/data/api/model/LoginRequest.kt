package com.ddd.attendance.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val provider: String,
    val token: String
)
