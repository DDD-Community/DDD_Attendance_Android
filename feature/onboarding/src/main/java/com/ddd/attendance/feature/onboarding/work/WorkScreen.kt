package com.ddd.attendance.feature.onboarding.work

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun WorkScreen() {
    Content()
}

@Composable
internal fun Content() {
    Text(
        text = "업무 선택 화면",
        color = Color.White
    )
}