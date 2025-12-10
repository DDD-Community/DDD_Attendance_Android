package com.ddd.attendance.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
            whiteCount = uiState.whiteBlockCount,
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
    whiteCount: Int,
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
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.left_arrow_white),
                contentDescription = "뒤로가기",
                modifier = Modifier.fillMaxSize()
            )
        }

        // step != 0일 때만 표시
        if (whiteCount > 0 || blackCount < 3) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(whiteCount) { index ->
                    // 마지막 흰색 블록이면서, 뒤에 검정 블록이 없으면 Spacer(간격) 생기지 않도록 설정
                    val isLastWhiteBlockWithoutBlack = index == whiteCount - 1 && blackCount == 0
                    BlockImage(
                        resId = R.drawable.step_block_white,
                        isLast = isLastWhiteBlockWithoutBlack
                    )
                }

                repeat(blackCount) { index ->
                    // 마지막 검정 블록이면 Spacer(간격) 생기지 않도록 설정
                    val isLastBlackBlock = index == blackCount - 1
                    BlockImage(
                        resId = R.drawable.step_block_black,
                        isLast = isLastBlackBlock
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(16.dp))

        // 다음 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    color = if (isNextEnabled) Color.Blue else Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(enabled = isNextEnabled) { onNextClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "다음",
                color = if (isNextEnabled) Color.White else Color.DarkGray,
                style = MaterialTheme.typography.bodyLarge
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
