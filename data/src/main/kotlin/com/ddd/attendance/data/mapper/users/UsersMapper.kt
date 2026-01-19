package com.ddd.attendance.data.mapper.users

import com.ddd.attendance.data.model.user.UsersResponse
import com.ddd.attendance.domain.model.users.Users

fun UsersResponse.toDomain(): Users {
    return Users(
        userId = userId?.toInt() ?: 0,
        name = name ?: "",
        email = email ?: "",
        generation = generation ?: "",
        team = team ?: "",
        jobRole = jobRole ?: "",
        role = role ?: "",
        managerRoles = managerRoles ?: emptyList()
    )
}