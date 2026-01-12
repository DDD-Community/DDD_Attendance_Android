package com.ddd.attendance.domain.model.onboarding

data class VerifyCode(
    val generationId: Int,
    val generationName: String,
    val type: String,
    val description: String
)