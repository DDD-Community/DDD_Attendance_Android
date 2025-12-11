package com.ddd.attendance.feature.onboarding.work

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ddd.attendance.feature.designsystem.component.DDDText

@Composable
internal fun WorkScreen() {
    Content()
}

@Composable
internal fun Content() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DDDText(
            text = "업무 선택 화면",
        )
    }
}