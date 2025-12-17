package com.ddd.attendance.feature.onboarding.invite

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DDDText
import com.ddd.attendance.feature.designsystem.theme.DDDColor
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.onboarding.OnBoardingStep
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
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(40.dp))

        DDDText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.enter_invite_code),
            style = Typography.titleLargeB,
            color = DDDColor.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        DDDText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.invite_code_description),
            style = Typography.bodySmallM,
            color = DDDColor.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        InputPin(
            value = pinCode,
            pinCodeStatus = pinCodeStatus,
            focusRequester = focusRequester,
            keyboardController = keyboardController,
            onValueChange = onInvitePinCodeChanged
        )

        if (pinCodeStatus == PinCodeStatus.Fail) {
            Spacer(modifier = Modifier.height(12.dp))
            PinError()
        }
    }
}

@Composable
private fun InputPin(
    value: String,
    pinCodeStatus: PinCodeStatus,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= 4 && newValue.all(Char::isDigit)) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .size(1.dp)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        PinBoxes(
            pin = value,
            pinCodeStatus = pinCodeStatus,
            onPinClear = { onValueChange("") },
            onRequestFocus = {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        )
    }
}

@Composable
private fun PinBoxes(
    pin: String,
    pinCodeStatus: PinCodeStatus,
    onPinClear: () -> Unit,
    onRequestFocus: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(4) { index ->
            val char = pin.getOrNull(index)?.toString().orEmpty()

            val isFilled = index < pin.length

            val borderColor = when {
                pinCodeStatus == PinCodeStatus.Fail -> DDDColor.Fail
                isFilled -> DDDColor.BorderEnabled
                else -> DDDColor.BorderDisabled
            }

            val backgroundColor = when {
                pinCodeStatus == PinCodeStatus.Fail -> DDDColor.BorderFailBackground
                isFilled -> DDDColor.BorderEnableBackground
                else -> DDDColor.Transparent
            }

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .background(backgroundColor, RoundedCornerShape(16.dp))
                    .clickable {
                        onRequestFocus()
                        onPinClear()
                    },
                contentAlignment = Alignment.Center
            ) {
                DDDText(
                    text = char,
                    style = Typography.headlineSmallB,
                    color = DDDColor.Black
                )
            }
        }
    }
}

@Composable
private fun PinError() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.error),
            contentDescription = null
        )
        DDDText(
            text = stringResource(R.string.code_invalid),
            style = Typography.bodyMediumM,
            color = DDDColor.Fail
        )
    }
}
