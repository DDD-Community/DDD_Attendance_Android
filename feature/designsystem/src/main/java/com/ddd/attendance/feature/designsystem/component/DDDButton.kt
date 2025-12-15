package com.ddd.attendance.feature.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.theme.DDDColor
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
fun DDDButton(
    modifier: Modifier = Modifier,
    text: String = "다음",
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    height: Dp = 48.dp,
    shape: Shape = RoundedCornerShape(percent = 50),
    enabledColor: Color = DDDColor.ButtonEnabled,
    disabledColor: Color = DDDColor.ButtonDisabled,
    textColor: Color = DDDColor.White,
    textStyle: TextStyle = Typography.bodyMediumM,
    debounceMillis: Long = 500L
) {
    val lastClickTime = remember { mutableStateOf(0L) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                color = if (isEnabled) enabledColor else disabledColor,
                shape = shape
            ).clickable {
                if (!isEnabled) return@clickable

                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime.value >= debounceMillis) {
                    lastClickTime.value = currentTime
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        DDDText(
            text = text,
            color = textColor,
            style = textStyle
        )
    }
}