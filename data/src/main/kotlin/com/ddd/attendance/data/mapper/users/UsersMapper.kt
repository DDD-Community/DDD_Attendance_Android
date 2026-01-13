package com.ddd.attendance.data.mapper.users

import com.ddd.attendance.data.model.UsersResponse
import com.ddd.attendance.domain.model.users.Users

fun UsersResponse.toDomain(): Users {
    return Users(
        userId = userId,
        name = name,
        email = email,
        generation = generation,
        team = team,
        jobRole = jobRole,
        role = role,
        managerRoles = managerRoles
    )
}