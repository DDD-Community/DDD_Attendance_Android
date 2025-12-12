package com.ddd.attendance.feature.onboarding.invite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DDDText
import com.ddd.attendance.feature.designsystem.theme.DDDColor
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.onboarding.R

@Composable
internal fun InviteScreen(
    pinCodeStatus: PinCodeStatus,
    pinCode: String,
    onInvitePinCodeChanged: (pinCode: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    Content(
        pinCodeStatus = pinCodeStatus,
        pinCode = pinCode,
        focusRequester = focusRequester,
        keyboardController = softwareKeyboardController,
        onInvitePinCodeChanged = onInvitePinCodeChanged
    )
}

@Composable
internal fun Content(
    pinCodeStatus: PinCodeStatus,
    pinCode: String,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onInvitePinCodeChanged: (pinCode: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        DDDText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.enter_invite_code),
            style = Typography.titleLargeB,
            color = DDDColor.TextPrimary,

        )

        Spacer(modifier = Modifier.height(8.dp))

        DDDText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.invite_code_description),
            style = Typography.bodySmallM,
            color = DDDColor.TextSecondary
        )

        Spacer(modifier = Modifier.height(40.dp))

        InputPin(
            pinCodeStatus = pinCodeStatus,
            value = pinCode,
            onValueChange = {
                onInvitePinCodeChanged(it)
            },
            focusRequester = focusRequester,
            keyboardController = keyboardController
        )

        if (pinCodeStatus == PinCodeStatus.Fail) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.error),
                    contentDescription = "오류 발생",
                )

                DDDText(
                    text = stringResource(R.string.code_invalid),
                    style = Typography.bodyMediumM,
                    color = DDDColor.Fail
                )
            }
        }
    }
}

@Composable
internal fun InputPin(
    pinCodeStatus: PinCodeStatus,
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .size(1.dp)
                .alpha(0f)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        keyboardController?.show()
                    }
                },
            value = value,
            onValueChange = {
                if (it.length <= 4) {
                    onValueChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(4) { index ->
                val char = value.getOrNull(index)?.toString()?: ""

                val borderColor = when(pinCodeStatus) {
                    PinCodeStatus.Fail -> DDDColor.Fail
                    else -> if (index <= value.length - 1) {
                        DDDColor.ButtonEnabled
                    } else {
                        DDDColor.ButtonDisabled
                    }
                }

                val backgroundColor = when(pinCodeStatus) {
                    PinCodeStatus.Fail -> DDDColor.BorderFailBackground
                    else -> if (index <= value.length - 1) {
                        DDDColor.BorderEnableBackground
                    } else {
                        DDDColor.Transparent
                    }
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                // 포커스 요청 + 키보드 강제 표시
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    DDDText(
                        text = char,
                        style = Typography.headlineSmallB,
                        color = DDDColor.TextSecondary
                    )
                }
            }
        }
    }
}