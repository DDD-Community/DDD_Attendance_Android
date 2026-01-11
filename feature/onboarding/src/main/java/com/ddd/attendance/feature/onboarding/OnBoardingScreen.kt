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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.onboarding.invite.InviteScreen
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import com.ddd.attendance.feature.onboarding.name.NameScreen
import com.ddd.attendance.feature.onboarding.select.SelectItemUiModel
import com.ddd.attendance.feature.onboarding.select.SelectionScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList


@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.PopBackStack -> {
                    navController.popBackStack()
                }
                NavigationEvent.GoToHome -> {
                    navController.navigate("ADMIN_MAIN")
                }
            }
        }
    }

    Content(
        modifier = modifier,
        step = uiState.step,
        grayBlockCount = uiState.grayBlockCount,
        blackBlockCount = uiState.blackBlockCount,

        selectList = uiState.currentSelectItems,
        onItemClick = { position ->
            viewModel.onIntent(OnBoardingIntent.SelectListItem(position))
        },

        canGoNext = uiState.canGoNext,
        onBackClick = { viewModel.onIntent(OnBoardingIntent.GoToPreviousStep) },
        onNextClick = { viewModel.onIntent(OnBoardingIntent.GoToNextStep) },

        pinCodeStatus = uiState.pinCodeStatus,
        pinCode = uiState.inputInvitePinCode,
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
internal fun Content(
    modifier: Modifier = Modifier,
    step: OnBoardingStep = OnBoardingStep.Invite,
    selectList: ImmutableList<SelectItemUiModel>,
    onItemClick:(position: Int) -> Unit,
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
    Column(modifier.fillMaxSize()) {
        OnBoardingHeader(
            grayBlockCount = grayBlockCount,
            blackBlockCount = blackBlockCount,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.weight(1f))

        OnBoardingBody(
            step = step,
            selectList = selectList,
            onItemClick = onItemClick,
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
    modifier: Modifier = Modifier,
    grayBlockCount: Int,
    blackBlockCount: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
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

        StepBlocks(grayBlockCount = grayBlockCount, blackBlockCount = blackBlockCount)
    }
}

@Composable
internal fun OnBoardingBody(
    modifier: Modifier = Modifier,
    step: OnBoardingStep,
    selectList: ImmutableList<SelectItemUiModel>,
    onItemClick:(position: Int) -> Unit,
    isNextEnabled: Boolean,
    pinCodeStatus: PinCodeStatus,
    pinCode: String,
    onInvitePinCodeChanged: (String) -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    onNextClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
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
                        onInvitePinCodeChanged = onInvitePinCodeChanged
                    )
                }
                OnBoardingStep.Name -> {
                    NameScreen(
                        name = name,
                        onNameChanged = onNameChanged
                    )
                }
                OnBoardingStep.Job, OnBoardingStep.Team, OnBoardingStep.Role -> {
                    SelectionScreen(
                        step = step,
                        items = selectList.toPersistentList(),
                        onClick = {
                            onItemClick(it)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        DddLargeSizeButton(
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
    modifier: Modifier = Modifier,
    grayBlockCount: Int,
    blackBlockCount: Int
) {
    if (grayBlockCount + blackBlockCount > 0 && blackBlockCount != 3) {
        Row(
            modifier = modifier
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
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
    isLast: Boolean = false
) {
    Image(
        modifier = modifier
            .height(4.dp)
            .padding(end = if (isLast) 0.dp else 2.dp),
        painter = painterResource(resId),
        contentDescription = null
    )
}
