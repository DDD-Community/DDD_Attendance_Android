package com.ddd.attendance.feature.admin.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.core.contributor.ContributorBottomSheet
import com.ddd.attendance.feature.designsystem.component.DddIconButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryLight
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
fun AdminProfileScreen(
    navController: NavController,
    viewModel: AdminProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.PopBackStack -> { navController.popBackStack() }
            }
        }
    }
    Content(
        name = uiState.name,
        position = uiState.position,
        team = uiState.name,
        task = uiState.task,
        generation = uiState.generation,
        organization = uiState.organization,
        version = uiState.appInfo.version,
        privacyPolicyText = uiState.appInfo.privacyPolicyText,
        privacyPolicyUrl = uiState.appInfo.privacyPolicyUrl,
        isShowContributorBottomSheet = uiState.isShowContributorBottomSheet,
        onBackClick = {
            viewModel.onIntent(AdminProfileIntent.PopBackStack)
        },
        onInfoClick  = {
            viewModel.onIntent(AdminProfileIntent.ShowContributorBottomSheet)
        },
        onContributorBottomSheetDismiss = {
            viewModel.onIntent(AdminProfileIntent.HideContributorBottomSheet)
        }
    )
}

@Composable
private fun Content(
    name: String,
    position: String,
    team: String,
    task: String,
    generation: String,
    organization: String,
    version: String,
    privacyPolicyUrl: String,
    privacyPolicyText: String,
    isShowContributorBottomSheet: Boolean,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onContributorBottomSheetDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            AdminProfileHeader(
                onBackClick = onBackClick,
                onInfoClick = onInfoClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminProfileCard(
                modifier = Modifier.padding(horizontal = 24.dp),
                name = name,
                position = position,
                team = team,
                task = task,
                generation = generation,
                organization = organization
            )

            Spacer(modifier = Modifier.height(36.dp))

            AdminBottomSection(
                version = version,
                privacyPolicyText = privacyPolicyText,
                privacyPolicyUrl = privacyPolicyUrl
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        ContributorBottomSheet(
            isShow = isShowContributorBottomSheet,
            onFeedback = {

            },
            onDismiss = onContributorBottomSheetDismiss
        )
    }
}

@Composable
private fun AdminProfileHeader(
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
        DddIconButton(
            modifier = Modifier
                .size(24.dp),
            enabledIconRes = com.ddd.attendance.feature.core.R.drawable.ic_chevron_white,
            disabledIconRes = com.ddd.attendance.feature.core.R.drawable.ic_chevron_white,
        ) {
            onBackClick()
        }

        DddIconButton(
            modifier = Modifier
                .size(24.dp),
            enabledIconRes = com.ddd.attendance.feature.core.R.drawable.ic_info,
            disabledIconRes = com.ddd.attendance.feature.core.R.drawable.ic_info,
        ) {
            onInfoClick()
        }
    }
}

@Composable
fun AdminProfileCard(
    modifier: Modifier = Modifier,
    name: String,
    position: String,
    team: String,
    task: String,
    generation: String,
    organization: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFF1976D2)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = ButtonEnabled,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DddText(
                        text = stringResource(R.string.admin),
                        style = Typography.bodySmallM,
                        color = ButtonEnabled
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xB20D82F9),
                            shape = RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = com.ddd.attendance.feature.core.R.drawable.ic_edit),
                            contentDescription = "수정 아이콘",
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        DddText(
                            text = stringResource(R.string.edit_generation),
                            style = Typography.bodySmallM
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            DddText(
                text = name,
                style = Typography.headlineLargeB,
                color = BackgroundSecondaryDark
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProfileInfoSection(
                label = stringResource(R.string.job_role),
                value = position
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProfileInfoSection(
                label = stringResource(R.string.assigned_team),
                value = team
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProfileInfoSection(
                label = stringResource(R.string.generation),
                value = generation
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                DddText(
                    text = stringResource(R.string.assigned_task),
                    style = Typography.bodySmallM,
                    color = TextSecondaryLight
                )

                Spacer(modifier = Modifier.height(2.dp))

                DddText(
                    text = task,
                    style = Typography.bodySmallR,
                    color = BackgroundSecondaryDark
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            DddText(
                modifier = Modifier.fillMaxWidth(),
                text = organization,
                style = Typography.bodySmallR,
                color = TextSecondaryLight,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun AdminBottomSection(
    modifier: Modifier = Modifier,
    version: String,
    privacyPolicyText: String,
    privacyPolicyUrl: String
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
            DddText(
                modifier = Modifier
                    .padding(
                        horizontal = 28.dp,
                        vertical = 12.dp
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        /* TODO: 탈퇴하기 클릭 */
                    },
                text = stringResource(R.string.withdraw_account),
                style = Typography.titleSmallM,
                color = Color(0xFF8E8E93),
                textDecoration = TextDecoration.Underline
            )

            DddText(
                modifier = Modifier
                    .padding(
                        horizontal = 28.dp,
                        vertical = 12.dp
                    ).clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        /* TODO: 로그아웃 클릭 */
                    },
                text = stringResource(R.string.logout),
                style = Typography.titleSmallM,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Version $version",
            fontSize = 14.sp,
            color = Color(0xFFC6C6CF),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = privacyPolicyText,
            fontSize = 14.sp,
            color = Color(0xFF8E8E93),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable {
                    openUrl(context, privacyPolicyUrl)
                }
        )
    }
}

@Composable
private fun ProfileInfoSection(
    label: String,
    value: String
) {
    Column {
        DddText(
            text = label,
            style = Typography.bodySmallM,
            color = TextSecondaryLight
        )

        Spacer(modifier = Modifier.height(2.dp))

        DddText(
            text = value,
            style = Typography.titleMediumM,
            color = BackgroundSecondaryDark
        )
    }
}

private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        // URL 열기 실패 시 처리
        e.printStackTrace()
    }
}