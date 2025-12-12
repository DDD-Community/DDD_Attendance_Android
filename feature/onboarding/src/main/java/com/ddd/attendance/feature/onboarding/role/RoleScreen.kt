package com.ddd.attendance.feature.onboarding.role

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ddd.attendance.feature.designsystem.component.DDDText

@Composable
internal fun RoleScreen() {
    Content()
}

@Composable
internal fun Content() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DDDText(
            text = "직무 선택 화면",
        )
    }
}