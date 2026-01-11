package com.ddd.attendance.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class JobRoleResponse(
    val key: String,
    val description: String
)