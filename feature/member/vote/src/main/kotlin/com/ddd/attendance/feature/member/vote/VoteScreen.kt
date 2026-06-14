package com.ddd.attendance.feature.member.vote

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.core.popup.TwoButtonTitleContentPopup
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundDefault
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.member.vote.step.Step1TeamVoteScreen
import com.ddd.attendance.feature.member.vote.step.Step2FeedbackScreen
import com.ddd.attendance.feature.member.vote.step.VoteCompleteScreen

@Composable
fun VoteScreen(
    navController: NavController,
    onApiErrorMessage: (Throwable) -> Unit = {},
    viewModel: VoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is VoteNavigationEvent.PopBackStack -> navController.popBackStack()
                is VoteNavigationEvent.OnApiErrorMessage -> onApiErrorMessage(event.throwable)
            }
        }
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onIntent(VoteIntent.ToastShown)
        }
    }

    // STEP 작성 중에는 뒤로가기 시 확인 다이얼로그
    BackHandler(enabled = uiState.step == VoteStep.STEP1 || uiState.step == VoteStep.STEP2) {
        viewModel.onIntent(VoteIntent.BackPressed)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        when (uiState.step) {
            VoteStep.LOADING -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = ButtonEnabled
            )

            VoteStep.ERROR -> DddText(
                text = "진행 중인 투표가 없어요.",
                style = Typography.bodyMediumM,
                color = TextSecondaryDark,
                modifier = Modifier.align(Alignment.Center)
            )

            VoteStep.STEP1 -> Step1TeamVoteScreen(
                uiState = uiState,
                onIntent = viewModel::onIntent,
                onBack = { viewModel.onIntent(VoteIntent.BackPressed) }
            )

            VoteStep.STEP2 -> Step2FeedbackScreen(
                uiState = uiState,
                onIntent = viewModel::onIntent,
                onBack = { viewModel.onIntent(VoteIntent.BackPressed) }
            )

            VoteStep.COMPLETE -> VoteCompleteScreen(
                onConfirm = { navController.popBackStack() }
            )
        }
    }

    TwoButtonTitleContentPopup(
        isShow = uiState.isShowExitDialog,
        titleText = "정말 뒤로 가시겠어요?",
        contentText = "지금 나가면 작성 중인 내용이 저장되지 않아요.",
        confirmText = "계속 작성",
        cancelText = "나가기",
        onConfirm = { viewModel.onIntent(VoteIntent.DismissExit) },
        onCancel = { viewModel.onIntent(VoteIntent.ConfirmExit) }
    )
}
