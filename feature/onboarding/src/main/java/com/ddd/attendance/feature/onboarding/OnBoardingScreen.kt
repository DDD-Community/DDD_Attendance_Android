package com.ddd.attendance.feature.onboarding

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.designsystem.component.DDDButton
import com.ddd.attendance.feature.onboarding.invite.InviteScreen
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import com.ddd.attendance.feature.onboarding.name.NameScreen
import com.ddd.attendance.feature.onboarding.role.RoleScreen
import com.ddd.attendance.feature.onboarding.team.TeamScreen
import com.ddd.attendance.feature.onboarding.work.WorkScreen


@Composable
fun OnBoardingScreen(
    navController: NavController,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnBoardingScreenContent(
        step = uiState.step,
        canGoNext = uiState.canGoNext,
        grayBlockCount = uiState.grayBlockCount,
        blackBlockCount = uiState.blackBlockCount,
        pinCodeStatus = uiState.pinCodeStatus,
        onBackClick = {
            if (uiState.step == OnBoardingStep.Invite) {
                navController.popBackStack()
            } else {
                viewModel.onIntent(OnBoardingIntent.BackStepBlock)
            }
        },
        onNextClick = {
            viewModel.onIntent(OnBoardingIntent.NextStepBlock)
        },
        pinCode = uiState.invitePinCode,
        onInvitePinCodeChanged = {
            viewModel.onIntent(OnBoardingIntent.InvitePinCodeChanged(it))
        },
        name = uiState.name,
        onNameChanged = {
            viewModel.onIntent(OnBoardingIntent.NameChanged(it))
        }
    )
}

@Composable
internal fun OnBoardingScreenContent(
    step: OnBoardingStep = OnBoardingStep.Invite,
    grayBlockCount: Int,
    blackBlockCount: Int,
    canGoNext: Boolean,
    pinCodeStatus: PinCodeStatus,
    pinCode: String,
    onInvitePinCodeChanged: (String) -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        OnBoardingHeader(
            grayBlockCount = grayBlockCount,
            blackBlockCount = blackBlockCount,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.weight(1f))

        OnBoardingBody(
            step = step,
            onNextClick = onNextClick,
            isNextEnabled = canGoNext,
            pinCode = pinCode,
            pinCodeStatus = pinCodeStatus,
            onInvitePinCodeChanged = onInvitePinCodeChanged,
            name = name,
            onNameChanged = onNameChanged
        )
    }
}

@Composable
internal fun OnBoardingHeader(
    grayBlockCount: Int,
    blackBlockCount: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, end = 12.dp)
                .size(28.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.left_arrow_white),
                contentDescription = "뒤로가기",
                modifier = Modifier.fillMaxSize()
            )
        }

        Log.d("블럭체크", "$grayBlockCount, $blackBlockCount")
        StepBlocks(grayBlockCount = grayBlockCount, blackBlockCount = blackBlockCount)
    }
}

@Composable
internal fun OnBoardingBody(
    step: OnBoardingStep,
    isNextEnabled: Boolean,
    pinCodeStatus: PinCodeStatus,
    pinCode: String,
    onInvitePinCodeChanged: (String) -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    onNextClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                OnBoardingStep.Invite -> InviteScreen(pinCodeStatus, pinCode, onInvitePinCodeChanged)
                OnBoardingStep.Name -> NameScreen(name, onNameChanged)
                OnBoardingStep.Role -> RoleScreen()
                OnBoardingStep.Team -> TeamScreen()
                OnBoardingStep.Work -> WorkScreen()
            }

            Spacer(modifier = Modifier.weight(1f)) // 남은 공간을 밀어서 버튼이 아래로
        }

        DDDButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 20.dp),
            text = stringResource(R.string.next),
            isEnabled = isNextEnabled,
            onClick = {
                onNextClick()
            }
        )
    }
}

@Composable
fun StepBlocks(
    grayBlockCount: Int,
    blackBlockCount: Int
) {
    if (grayBlockCount + blackBlockCount > 0 && blackBlockCount != 3) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(grayBlockCount) { index ->
                BlockImage(
                    resId = R.drawable.step_block_gray,
                    isLast = index == grayBlockCount - 1
                )
            }

            repeat(blackBlockCount) { index ->
                BlockImage(
                    resId = R.drawable.step_block_black,
                    isLast = index == blackBlockCount - 1
                )
            }
        }
    }
}

@Composable
private fun BlockImage(
    @DrawableRes resId: Int,
    isLast: Boolean = false
) {
    Image(
        painter = painterResource(resId),
        contentDescription = null,
        modifier = Modifier
            .height(4.dp)
            .padding(end = if (isLast) 0.dp else 2.dp)
    )
}
