package com.ddd.attendance.data.mapper.users

import com.ddd.attendance.data.model.user.UsersMeResponse
import com.ddd.attendance.domain.model.users.UsersMe

fun UsersMeResponse.toDomain(): UsersMe {
    return UsersMe(
        userId = userId?: 0,
        name = name.orEmpty(),
        email = email.orEmpty(),
        generation = generation.orEmpty(),
        team = team.orEmpty(),
        jobRole = jobRole.orEmpty(),
        role = role.orEmpty()
    )
}