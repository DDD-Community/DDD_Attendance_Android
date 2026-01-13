package com.ddd.attendance.data.mapper.onboarding

import com.ddd.attendance.data.model.JobRoleResponse
import com.ddd.attendance.data.model.TeamResponse
import com.ddd.attendance.data.model.VerifyCodeResponse
import com.ddd.attendance.domain.model.onboarding.ItemSelect
import com.ddd.attendance.domain.model.onboarding.VerifyCode

fun VerifyCodeResponse.toDomain(): VerifyCode {
    return VerifyCode(
        generationId = generationId,
        generationName = generationName,
        type = type,
        description = description
    )
}

fun List<TeamResponse>.toItemSelectTeamDomain(): List<ItemSelect> {
    return map { response ->
        ItemSelect(
            teamId = response.teamId,
            name = response.name
        )
    }
}

fun List<JobRoleResponse>.toItemSelectRoleDomain(): List<ItemSelect> {
    return map { response ->
        ItemSelect(
            key = response.key,
            name = response.description
        )
    }
}

fun List<JobRoleResponse>.toItemSelectJobDomain(): List<ItemSelect> {
    return map { response ->
        ItemSelect(
            key = response.key,
            name = response.description
        )
    }
}