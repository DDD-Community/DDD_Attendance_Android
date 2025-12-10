package com.ddd.attendance.feature.onboarding.role

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun RoleScreen() {
    Content()
}

@Composable
internal fun Content() {
    Text(
        text = "직무 선택 화면",
        color = Color.White
    )
}