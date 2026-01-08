package com.ddd.attendance.feature.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
fun DddText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = TextPrimary,
    style: TextStyle = Typography.bodySmallM,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign = TextAlign.Start,
    textDecoration: TextDecoration? = TextDecoration.None
) {
    Text(
        text = text,
        color = color,
        style = style,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        textDecoration = textDecoration
    )
}