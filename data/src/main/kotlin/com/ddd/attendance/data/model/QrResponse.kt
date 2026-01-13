package com.ddd.attendance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QrResponse(
    val id: Long,
    val qrBase64: String
)
