package com.ddd.attendance.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class VerifyCodeResponse(
    val generationId: Int,
    val generationName: String,
    val type: String,
    val description: String
)