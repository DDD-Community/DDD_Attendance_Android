package com.ddd.attendance.feature.onboarding.name

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ddd.attendance.feature.designsystem.component.DDDText

@Composable
internal fun NameScreen() {
    Content()
}

@Composable
internal fun Content() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DDDText(
            text = "사용자 이름 입력 화면",
        )
    }
}