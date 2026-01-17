package com.ddd.attendance.feature.member.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ddd.attendance.feature.core.contributor.ContributorBottomSheet
import com.ddd.attendance.feature.core.popup.TwoButtonTitleContentPopup
import com.ddd.attendance.feature.core.profile.ProfileCard
import com.ddd.attendance.feature.core.profile.ProfileData

@Composable
fun MemberProfileScreen(
    navController: NavController,
    viewModel: MemberProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                ProfileNavigationEvent.PopBackStack -> { navController.popBackStack() }
                ProfileNavigationEvent.GoToLogin -> {
                    navController.navigate("LOGIN") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                ProfileNavigationEvent.GoToOnboarding -> navController.navigate("ON_BOARDING")
                is ProfileNavigationEvent.ShowError -> {}
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MemberProfileHeader(
                onBackClick = { viewModel.popBackStack() },
                onInfoClick = { viewModel.showContributorBottomSheet(true) }
            )

            ProfileCard(
                modifier = Modifier.padding(all = 24.dp),
                profileData = ProfileData(
                    name = uiState.name,
                    position = uiState.position,
                    team = uiState.team,
                    generation = uiState.generation,
                    organization = uiState.organization
                )
            )

            MemberBottomSection(
                uiState = uiState,
                onWithdrawClick = { viewModel.showWithdrawAccountPopup(true) },
                onLogoutClick = { viewModel.showLogoutPopup(true) }
            )
        }

        ContributorBottomSheet(
            isShow = uiState.isShowContributorBottomSheet,
            onFeedback = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://forms.gle/a2idQmnxjbC5czfP7")
                    )
                )
            },
            onDismiss = { viewModel.showContributorBottomSheet(false) }
        )

        TwoButtonTitleContentPopup(
            isShow = uiState.isShowWithdrawAccountPopup,
            titleText = stringResource(com.ddd.attendance.feature.core.R.string.withdrawal_confirm_title),
            contentText = stringResource(com.ddd.attendance.feature.core.R.string.withdrawal_warning_content),
            confirmText = stringResource(com.ddd.attendance.feature.core.R.string.withdraw_account),
            onConfirm = { viewModel.onWithdrawAccount() },
            cancelText = stringResource(com.ddd.attendance.feature.core.R.string.cancel),
            onCancel = { viewModel.showWithdrawAccountPopup(false) }
        )

        TwoButtonTitleContentPopup(
            isShow = uiState.isShowLogoutPopup,
            titleText = stringResource(com.ddd.attendance.feature.core.R.string.logout_confirm_title),
            confirmText = stringResource(com.ddd.attendance.feature.core.R.string.logout),
            onConfirm = { viewModel.onLogout() },
            cancelText = stringResource(com.ddd.attendance.feature.core.R.string.cancel),
            onCancel = { viewModel.showLogoutPopup(false) }
        )
    }
}

@Composable
private fun MemberProfileHeader(
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = com.ddd.attendance.feature.core.R.drawable.ic_chevron_white),
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() },
            tint = Color.White
        )
        
        Icon(
            painter = painterResource(id = com.ddd.attendance.feature.core.R.drawable.ic_info),
            contentDescription = "Info",
            modifier = Modifier
                .size(24.dp)
                .clickable { onInfoClick() },
            tint = Color.White
        )
    }
}

@Composable
private fun MemberBottomSection(
    modifier: Modifier = Modifier,
    uiState: MemberProfileUiState,
    onWithdrawClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current

    // 하단 메뉴
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 탈퇴하기, 로그아웃을 가로로 배치
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 28.dp, vertical = 12.dp)
                    .clickable { onWithdrawClick() },
                text = "탈퇴하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8E8E93),
                textDecoration = TextDecoration.Underline,
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 28.dp, vertical = 12.dp)
                    .clickable { onLogoutClick() },
                text = "로그아웃",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Version ${uiState.appInfo.version}",
            fontSize = 14.sp,
            color = Color(0xFFC6C6CF),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = uiState.appInfo.privacyPolicyText,
            fontSize = 14.sp,
            color = Color(0xFF8E8E93),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(uiState.appInfo.privacyPolicyUrl)
                        )
                    )
                }
        )
    }
}