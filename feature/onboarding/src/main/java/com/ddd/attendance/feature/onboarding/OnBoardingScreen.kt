package com.ddd.attendance.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.designsystem.component.DDDButton
import com.ddd.attendance.feature.designsystem.component.DDDText
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.onboarding.invite.InviteScreen
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
        onBackClick = {
            if (uiState.step == 0) {
                navController.popBackStack()
            } else {
                viewModel.onIntent(OnBoardingIntent.BackStepBlock)
            }
        },
        onNextClick = {
            viewModel.onIntent(OnBoardingIntent.NextStepBlock)
        }
    )
}

@Composable
internal fun OnBoardingScreenContent(
    uiState: OnBoardingUiState,
    onBackClick:() -> Unit,
    onNextClick:() -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        OnBoardingHeader(
            grayCount = uiState.grayBlockCount,
            blackCount = uiState.blackBlockCount,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.weight(1f))

        OnBoardingBody(
            type = uiState.type,
            step = uiState.step,
            onNextClick = onNextClick,
            isNextEnabled = true
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
    type: OnBoardingType,
    step: Int,
    isNextEnabled: Boolean,
    onNextClick:() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                0 -> InviteScreen()
                1 -> NameScreen()
                2 -> RoleScreen()
                3 -> {
                    when(type) {
                        OnBoardingType.Admin -> WorkScreen()
                        OnBoardingType.Member -> TeamScreen()
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 20.dp, end = 24.dp)
        ) {
            DDDButton(
                text = "다음",
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
