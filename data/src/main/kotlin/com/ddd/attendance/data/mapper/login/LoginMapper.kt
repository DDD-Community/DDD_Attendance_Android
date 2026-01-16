package com.ddd.attendance.data.mapper.login

import com.ddd.attendance.data.model.LoginResponse
import com.ddd.attendance.domain.model.Login

fun LoginResponse.toDomain(statusCode: Int): Login =
    Login(
        statusCode = statusCode,
        userId = userId ?: 0L,
        name = name.orEmpty(),
        email = email.orEmpty(),
        oauthProvider = oauthProvider.orEmpty(),
        message = message.orEmpty(),
        isNewUser = isNewUser ?: false,
        accessToken = accessToken.orEmpty(),
        refreshToken = refreshToken.orEmpty(),
        oauthRefreshToken = oauthRefreshToken.orEmpty(),
        role = role.orEmpty()
    )