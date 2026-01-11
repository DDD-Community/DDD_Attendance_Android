package com.ddd.attendance.data.mapper

import com.ddd.attendance.data.model.JobRoleResponse
import com.ddd.attendance.data.model.TeamResponse
import com.ddd.attendance.data.model.VerifyCodeResponse
import com.ddd.attendance.domain.model.ItemSelect
import com.ddd.attendance.domain.model.VerifyCode

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
            id = response.teamId,
            name = response.name
        )
    }
}

fun List<JobRoleResponse>.toItemSelectRoleDomain(): List<ItemSelect> {
    return map { response ->
        ItemSelect(
            name = response.description
        )
    }
}

fun List<JobRoleResponse>.toItemSelectJobDomain(): List<ItemSelect> {
    return map { response ->
        ItemSelect(
            name = response.description
        )
    }
}