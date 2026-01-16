package com.ddd.attendance.domain.model

data class Login(
    val statusCode: Int,
    val userId: Long,
    val name: String,
    val email: String,
    val oauthProvider: String,
    val message: String,
    val isNewUser: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val oauthRefreshToken: String,
    val role: String
)