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

val Typography: DDDTypography = DDDTypography(
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
data class DDDTypography(
    val headlineSmallB: TextStyle,
    val titleLargeB: TextStyle,
    val bodyMediumM: TextStyle,
    val bodySmallM: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    DDDTypography(
        headlineSmallB = SansSerifStyle,
        titleLargeB = SansSerifStyle,
        bodyMediumM = SansSerifStyle,
        bodySmallM = SansSerifStyle,
    )
}
