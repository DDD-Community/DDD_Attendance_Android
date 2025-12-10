package com.ddd.attendance.feature.onboarding.name

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun NameScreen() {
    Content()
}

@Composable
internal fun Content() {
    Text(
        text = "사용자 이름 입력 화면",
        color = Color.White
    )
}