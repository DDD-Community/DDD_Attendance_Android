package com.ddd.attendance.feature.admin.main.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ddd.attendance.feature.admin.main.AdminType
import com.ddd.attendance.feature.admin.main.R
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BorderAlternative
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.designsystem.theme.White

@Composable
internal fun EditPopupDropdown(
    anchorWidth: Dp,
    anchorHeightPx: Int,
    expanded: Boolean,
    items: List<String>,
    onDismiss: () -> Unit,
    onItemSelected: (String) -> Unit
) {
    if (!expanded) return

    val density = LocalDensity.current
    val spacingPx = with(density) { 8.dp.toPx().toInt() }

    Popup(
        offset = IntOffset(
            x = 0,
            y = anchorHeightPx + spacingPx
        ),
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Surface(
            modifier = Modifier.width(anchorWidth),
            shape = RoundedCornerShape(16.dp),
            color = White,
            tonalElevation = 0.dp,
            shadowElevation = 4.dp
        ) {
            Column {
                items.forEachIndexed { index, text ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clickable {
                                onItemSelected(text)
                            }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        DddText(
                            text = text,
                            style = Typography.bodySmallB,
                            color = BackgroundSecondaryDark
                        )
                    }

                    if (index < items.lastIndex) {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(BorderAlternative)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ScreenChangeDropDown(
    modifier: Modifier = Modifier,
    isShow: Boolean,
    onScreenChangeDropDownDismiss: () -> Unit,
    onUiTypeChanged: (AdminType) -> Unit
) {
    if (!isShow) return

    val density = LocalDensity.current

    Popup(
        offset = IntOffset(
            x = with(density) { 24.dp.toPx().toInt() },
            y = with(density) { 52.dp.toPx().toInt() }
        ),
        onDismissRequest = onScreenChangeDropDownDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Surface(
            modifier = Modifier.width(131.dp),
            shape = RoundedCornerShape(16.dp),
            color = BorderDisabled,
            tonalElevation = 0.dp,
            shadowElevation = 4.dp
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable {
                            onUiTypeChanged(AdminType.Attendance)
                            onScreenChangeDropDownDismiss()
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    DddText(
                        text = stringResource(R.string.attendance),
                        style = Typography.titleSmallB
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(BackgroundSecondaryDark)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable {
                            onUiTypeChanged(AdminType.Schedule)
                            onScreenChangeDropDownDismiss()
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    DddText(
                        text = stringResource(R.string.schedule),
                        style = Typography.titleSmallB
                    )
                }
            }
        }
    }
}