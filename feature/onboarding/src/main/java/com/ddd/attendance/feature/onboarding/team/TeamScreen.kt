package com.ddd.attendance.feature.onboarding.team

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun TeamScreen() {
    Content()
}

@Composable
internal fun Content() {
    Text(
        text = "팀 선택 화면",
        color = Color.White
    )
}