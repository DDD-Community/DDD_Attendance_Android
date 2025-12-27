package com.ddd.attendance.feature.onboarding.name

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.Fail
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondary
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.onboarding.R

@Composable
internal fun NameScreen(
    modifier: Modifier = Modifier,
    name: String,
    onNameChanged: (String) -> Unit,
) {
    Content(
        name = name,
        onNameChanged = onNameChanged
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    name: String,
    onNameChanged: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        DddText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.enter_name),
            style = Typography.titleLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        DddText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.name_description),
            style = Typography.bodySmallM,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        NameField(
            value = name,
            onValueChange = onNameChanged
        )
    }
}

@Composable
internal fun NameField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    val isValid = value.length <= 5

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isValid) BorderDisabled else Fail,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (isValid) {
                    //문자만 입력 가능
                    onValueChange(
                        it.filter { ch -> ch.isLetter() }
                    )
                }
            },
            modifier = Modifier
                .weight(1F)
                .height(56.dp),
            textStyle = TextStyle(
                color = TextPrimary,
                fontSize = Typography.bodyMediumM.fontSize
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )

        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .width(24.dp)
                .height(24.dp)
                .then(
                    if (isValid) {
                        Modifier.clickable {
                            onValueChange("")
                        }
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!isValid) {
                Image(
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.error),
                    contentDescription = "오류 발생",
                )
            } else {
                Image(
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "전체 제거",
                )
            }
        }
    }
}