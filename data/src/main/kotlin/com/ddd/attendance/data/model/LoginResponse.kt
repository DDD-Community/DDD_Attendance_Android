package com.ddd.attendance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: Long?,
    val name: String?,
    val email: String?,
    val oauthProvider: String?,
    val message: String?,
    val isNewUser: Boolean?,
    val accessToken: String?,
    val refreshToken: String?,
    val oauthRefreshToken: String?,
    val role: String?
)