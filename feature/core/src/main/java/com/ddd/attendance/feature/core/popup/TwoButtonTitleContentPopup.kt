package com.ddd.attendance.feature.core.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ddd.attendance.feature.core.R
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryLight
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryLight
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
fun TwoButtonTitleContentPopup(
    modifier: Modifier = Modifier,
    isShow: Boolean,
    titleText: String,
    contentText: String? = null,
    confirmText: String = stringResource(R.string.confirm),
    cancelText: String = stringResource(R.string.cancel),
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (!isShow) return

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .padding(horizontal = 36.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = BackgroundSecondaryLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DddText(
                    text = titleText,
                    style = Typography.titleSmallB,
                    color = BackgroundSecondaryDark
                )

                if (contentText != null) {
                    Spacer(modifier = Modifier.height(4.dp))

                    DddText(
                        text = contentText,
                        style = Typography.bodySmallR,
                        color = TextSecondaryLight,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF323537)),
                        onClick = onConfirm
                    ) {
                        DddText(
                            text = confirmText,
                            style = Typography.bodySmallM
                        )
                    }

                    Button(
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D82F9)),
                        onClick = onCancel
                    ) {
                        DddText(
                            text = cancelText,
                            style = Typography.bodySmallM
                        )
                    }
                }
            }
        }
    }
}