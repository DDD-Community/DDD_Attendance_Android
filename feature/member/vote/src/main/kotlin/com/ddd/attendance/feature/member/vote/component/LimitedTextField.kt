package com.ddd.attendance.feature.member.vote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BorderEnabled
import com.ddd.attendance.feature.designsystem.theme.BorderInactive
import com.ddd.attendance.feature.designsystem.theme.FailError
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.Typography

/**
 * 글자수 카운터 + 검증 메시지가 달린 멀티라인 입력 필드.
 */
@Composable
internal fun LimitedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    helperText: String? = null,
    minHeight: Int = 120
) {
    val borderColor = when {
        isError -> FailError
        value.isNotEmpty() -> BorderEnabled
        else -> BorderInactive
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundSecondaryDark)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = minHeight.dp),
                textStyle = Typography.bodySmallM.copy(color = TextPrimary),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(BorderEnabled),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        DddText(
                            text = placeholder,
                            style = Typography.bodySmallM,
                            color = TextDisabled
                        )
                    }
                    inner()
                }
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DddText(
                text = helperText.orEmpty(),
                style = Typography.bodySmallR,
                color = if (isError) FailError else TextDisabled
            )
            DddText(
                text = "${value.length} / $maxLength",
                style = Typography.bodySmallR,
                color = if (isError) FailError else TextDisabled
            )
        }
    }
}
