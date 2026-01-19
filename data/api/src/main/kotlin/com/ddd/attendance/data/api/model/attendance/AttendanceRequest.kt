package com.ddd.attendance.data.api.model.attendance

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequest(
    val qrCode: String
)