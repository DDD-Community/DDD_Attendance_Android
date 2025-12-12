package com.ddd.attendance.feature.onboarding

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
        uiState = uiState,
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
        }
    )
}

@Composable
internal fun OnBoardingScreenContent(
    uiState: OnBoardingUiState,
    onBackClick:() -> Unit,
    onNextClick:() -> Unit,
    pinCodeStatus: PinCodeStatus,
    pinCode: String,
    onInvitePinCodeChanged: (pinCode: String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        OnBoardingHeader(
            grayCount = uiState.grayBlockCount,
            blackCount = uiState.blackBlockCount,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.weight(1f))

        OnBoardingBody(
            step = uiState.step,
            onNextClick = onNextClick,
            isNextEnabled = pinCodeStatus in listOf(PinCodeStatus.Ready, PinCodeStatus.Success), //다른 bool도 추가될 예정
            pinCode = pinCode,
            pinCodeStatus = pinCodeStatus,
            onInvitePinCodeChanged = onInvitePinCodeChanged
        )
    }
}

@Composable
internal fun OnBoardingHeader(
    grayCount: Int,
    blackCount: Int,
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
                painter = painterResource(id = R.drawable.left_arrow_black),
                contentDescription = "뒤로가기",
                modifier = Modifier.fillMaxSize()
            )
        }

        if (blackCount > 0 || grayCount < 3) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 검정 블록 먼저
                repeat(blackCount) { index ->
                    val isLastBlackBlock = index == blackCount - 1
                    BlockImage(
                        resId = R.drawable.step_block_black,
                        isLast = isLastBlackBlock
                    )
                }

                // 그 뒤에 회색 블록
                repeat(grayCount) { index ->
                    val isLastGrayBlockWithoutBlack = index == grayCount - 1
                    BlockImage(
                        resId = R.drawable.step_block_gray,
                        isLast = isLastGrayBlockWithoutBlack
                    )
                }
            }
        }
    }
}

@Composable
internal fun OnBoardingBody(
    step: OnBoardingStep,
    isNextEnabled: Boolean,
    onNextClick:() -> Unit,
    pinCodeStatus: PinCodeStatus,
    pinCode: String,
    onInvitePinCodeChanged: (pinCode: String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                OnBoardingStep.Invite -> {
                    InviteScreen(
                        pinCodeStatus = pinCodeStatus,
                        pinCode = pinCode,
                        { onInvitePinCodeChanged(it) }
                    )
                }
                OnBoardingStep.Name -> NameScreen()
                OnBoardingStep.Role -> RoleScreen()
                OnBoardingStep.Team -> TeamScreen()
                OnBoardingStep.Work -> WorkScreen()
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 20.dp, end = 24.dp)
        ) {
            DDDButton(
                text = stringResource(R.string.next),
                isEnabled = isNextEnabled,
                onClick = onNextClick
            )
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
