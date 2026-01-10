package com.ddd.attendance.data.mapper

import com.ddd.attendance.data.model.JobRoleResponse
import com.ddd.attendance.data.model.TeamResponse
import com.ddd.attendance.data.model.VerifyCodeResponse
import com.ddd.attendance.domain.model.JobRole
import com.ddd.attendance.domain.model.Team
import com.ddd.attendance.domain.model.VerifyCode

fun VerifyCodeResponse.toDomain(): VerifyCode {
    return VerifyCode(
        generationId = generationId,
        generationName = generationName,
        type = type,
        description = description
    )
}

fun List<TeamResponse>.toTeamDomain(): List<Team> {
    return map { response ->
        Team(
            teamId = response.teamId,
            name = response.name
        )
    }
}

fun List<JobRoleResponse>.toJobRoleDomain(): List<JobRole> {
    return map { response ->
        JobRole(
            key = response.key,
            description = response.description
        )
    }
}