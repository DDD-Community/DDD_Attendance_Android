package com.ddd.attendance.feature.onboarding.invite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ddd.attendance.feature.designsystem.component.DDDText

@Composable
internal fun InviteScreen() {
    Content()
}

@Composable
internal fun Content() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DDDText(
            text = "초대 코드 입력 화면",
        )
    }
}