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
    titleMediumM = SansSerifStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleSmallB = SansSerifStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleSmallM = SansSerifStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
    ),
    bodyLargeB = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
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
    bodySmallB = SansSerifStyle.copy(
        fontSize = 14.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Bold
    ),
    bodySmallM = SansSerifStyle.copy(
        fontSize = 14.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium
    ),
    bodySmallR = SansSerifStyle.copy(
        fontSize = 14.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Normal
    )
)

@Immutable
data class DddTypography(
    val headlineSmallB: TextStyle,
    val titleLargeB: TextStyle,
    val titleMediumB: TextStyle,
    val titleMediumM: TextStyle,
    val titleSmallB: TextStyle,
    val titleSmallM: TextStyle,
    val bodyLargeM: TextStyle,
    val bodyLargeB: TextStyle,
    val bodyMediumM: TextStyle,
    val bodySmallB: TextStyle,
    val bodySmallM: TextStyle,
    val bodySmallR: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    DddTypography(
        headlineSmallB = SansSerifStyle,
        titleLargeB = SansSerifStyle,
        titleMediumB = SansSerifStyle,
        titleMediumM = SansSerifStyle,
        titleSmallB = SansSerifStyle,
        titleSmallM = SansSerifStyle,
        bodyLargeB = SansSerifStyle,
        bodyLargeM = SansSerifStyle,
        bodyMediumM = SansSerifStyle,
        bodySmallB = SansSerifStyle,
        bodySmallM = SansSerifStyle,
        bodySmallR = SansSerifStyle
    )
}
