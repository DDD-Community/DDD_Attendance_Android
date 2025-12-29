package com.ddd.attendance.feature.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val SansSerifStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
)

val Typography: DddTypography = DddTypography(
    headlineSmallB = SansSerifStyle.copy(
        fontSize = 32.sp,
        lineHeight = 44.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleLargeB = SansSerifStyle.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleMediumB = SansSerifStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleSmallB = SansSerifStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    bodyLargeM = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyMediumM = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium
    ),
    bodySmallM = SansSerifStyle.copy(
        fontSize = 14.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium
    ),
)

@Immutable
data class DddTypography(
    val headlineSmallB: TextStyle,
    val titleLargeB: TextStyle,
    val titleMediumB: TextStyle,
    val titleSmallB: TextStyle,
    val bodyLargeM: TextStyle,
    val bodyMediumM: TextStyle,
    val bodySmallM: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    DddTypography(
        headlineSmallB = SansSerifStyle,
        titleLargeB = SansSerifStyle,
        titleMediumB = SansSerifStyle,
        titleSmallB = SansSerifStyle,
        bodyLargeM = SansSerifStyle,
        bodyMediumM = SansSerifStyle,
        bodySmallM = SansSerifStyle,
    )
}
