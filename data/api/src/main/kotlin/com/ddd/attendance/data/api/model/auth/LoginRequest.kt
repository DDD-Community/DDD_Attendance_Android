package com.ddd.attendance.data.api.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val provider: String,
    val token: String
)