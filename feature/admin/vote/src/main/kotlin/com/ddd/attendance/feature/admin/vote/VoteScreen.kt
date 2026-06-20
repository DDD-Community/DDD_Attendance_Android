package com.ddd.attendance.feature.admin.vote

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ddd.attendance.feature.admin.vote.model.VoteAttendanceStatus
import com.ddd.attendance.feature.admin.vote.model.VoteMember
import com.ddd.attendance.feature.admin.vote.screen.VoteDetailContent
import com.ddd.attendance.feature.admin.vote.screen.VoteListContent
import com.ddd.attendance.feature.admin.vote.screen.VoteResultsContent
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundDefault
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

private val VoteEndRed = Color(0xFFE55D5D)
private val AttendedBg = Color(0xFF1F3A2A)
private val AttendedText = Color(0xFF67D685)
private val LateBg = Color(0xFF3F311F)
private val LateText = Color(0xFFE5A547)
private val AbsentBg = Color(0xFF3F1F1F)
private val AbsentText = Color(0xFFE56565)
private val NoneBg = Color(0xFF323537)
private val NoneText = Color(0xFFC6C6C6)

@Composable
fun VoteScreen(
    modifier: Modifier = Modifier,
    onApiErrorMessage: (Throwable) -> Unit = {},
    viewModel: VoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is VoteNavigationEvent.OnApiErrorMessage -> onApiErrorMessage(event.throwable)
            }
        }
    }

    Content(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    uiState: VoteUiState,
    onIntent: (VoteIntent) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState.subScreen) {
            VoteSubScreen.Manage -> ManageContent(uiState, onIntent)
            VoteSubScreen.List -> VoteListContent(
                items = uiState.voteList,
                isLoading = uiState.isVoteListLoading,
                onItemClick = { voteId -> onIntent(VoteIntent.OpenVoteDetail(voteId)) },
                onBack = { onIntent(VoteIntent.BackToManage) }
            )
            VoteSubScreen.Detail -> VoteDetailContent(
                detail = uiState.detail,
                isLoading = uiState.isDetailLoading,
                onOpenResults = { voteId -> onIntent(VoteIntent.OpenResults(voteId)) },
                onBack = { onIntent(VoteIntent.BackToList) }
            )
            VoteSubScreen.Results -> VoteResultsContent(
                teamResults = uiState.teamResults,
                feedbackResults = uiState.feedbackResults,
                selectedTab = uiState.resultsTab,
                isLoading = uiState.isResultsLoading,
                onSelectTab = { tab -> onIntent(VoteIntent.SelectResultsTab(tab)) },
                onBack = { onIntent(VoteIntent.BackToManage) }
            )
        }

        StartConfirmDialog(
            isShow = uiState.isShowStartConfirmDialog,
            onConfirm = { onIntent(VoteIntent.ConfirmStartVote) },
            onDismiss = { onIntent(VoteIntent.DismissStartConfirmDialog) }
        )

        EndConfirmDialog(
            isShow = uiState.isShowEndConfirmDialog,
            onConfirm = { onIntent(VoteIntent.ConfirmEndVote) },
            onDismiss = { onIntent(VoteIntent.DismissEndConfirmDialog) }
        )

        NotParticipatedDialog(
            isShow = uiState.isShowNotParticipatedDialog,
            members = uiState.notParticipatedMembers,
            onDismiss = { onIntent(VoteIntent.DismissNotParticipatedDialog) }
        )
    }
}

@Composable
private fun ManageContent(
    uiState: VoteUiState,
    onIntent: (VoteIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        DddText(
            text = stringResource(R.string.vote_admin_title),
            style = Typography.titleLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        DddText(
            text = stringResource(R.string.vote_admin_description),
            style = Typography.bodySmallR,
            color = TextDisabled
        )

        Spacer(modifier = Modifier.height(20.dp))

        StatusCard(
            voteStatus = uiState.voteStatus,
            participationText = participationText(uiState)
        )

        if (uiState.voteStatus != VoteStatus.BEFORE) {
            Spacer(modifier = Modifier.height(16.dp))

            NotParticipatedButton(
                onClick = { onIntent(VoteIntent.ShowNotParticipatedDialog) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BottomActionButton(
            voteStatus = uiState.voteStatus,
            onStartClick = { onIntent(VoteIntent.StartVoteClicked) },
            onEndClick = { onIntent(VoteIntent.EndVoteClicked) }
        )
    }
}


@Composable
private fun participationText(uiState: VoteUiState): String {
    return when (uiState.voteStatus) {
        VoteStatus.BEFORE -> stringResource(R.string.vote_participation_empty)
        VoteStatus.IN_PROGRESS -> stringResource(
            R.string.vote_participation_in_progress,
            uiState.totalMembers,
            uiState.participatedMembers,
            uiState.participationRate
        )
        VoteStatus.CLOSED -> stringResource(
            R.string.vote_participation_closed,
            uiState.participatedMembers,
            uiState.participationRate
        )
    }
}

@Composable
private fun StatusCard(
    modifier: Modifier = Modifier,
    voteStatus: VoteStatus,
    participationText: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondaryDark)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DddText(
                text = stringResource(R.string.vote_status_label),
                style = Typography.bodyMediumM,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.weight(1f))

            VoteStatusChip(voteStatus = voteStatus)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderDisabled)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DddText(
                text = stringResource(R.string.vote_participation_label),
                style = Typography.bodyMediumM,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.weight(1f))

            DddText(
                text = participationText,
                style = Typography.bodyMediumM,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun VoteStatusChip(
    modifier: Modifier = Modifier,
    voteStatus: VoteStatus
) {
    val label = when (voteStatus) {
        VoteStatus.BEFORE -> stringResource(R.string.vote_status_before)
        VoteStatus.IN_PROGRESS -> stringResource(R.string.vote_status_in_progress)
        VoteStatus.CLOSED -> stringResource(R.string.vote_status_closed)
    }
    val backgroundColor = if (voteStatus == VoteStatus.IN_PROGRESS) ButtonEnabled else BorderDisabled

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        DddText(
            text = label,
            style = Typography.bodySmallM,
            color = TextPrimary
        )
    }
}

@Composable
private fun NotParticipatedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = BorderDisabled, shape = RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DddText(
            text = stringResource(R.string.vote_check_not_participated),
            style = Typography.bodyLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.size(8.dp))

        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.ic_right_arrow_white),
            contentDescription = null
        )
    }
}

@Composable
private fun BottomActionButton(
    voteStatus: VoteStatus,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit
) {
    when (voteStatus) {
        VoteStatus.BEFORE -> {
            DddLargeSizeButton(
                text = stringResource(R.string.vote_start_button),
                shape = RoundedCornerShape(16.dp),
                height = 58.dp,
                textStyle = Typography.bodyLargeB,
                onClick = onStartClick
            )
        }
        VoteStatus.IN_PROGRESS -> {
            DddLargeSizeButton(
                text = stringResource(R.string.vote_end_button),
                shape = RoundedCornerShape(16.dp),
                height = 58.dp,
                enabledColor = VoteEndRed,
                textStyle = Typography.bodyLargeB,
                onClick = onEndClick
            )
        }
        VoteStatus.CLOSED -> {
            DddLargeSizeButton(
                text = stringResource(R.string.vote_ended_button),
                shape = RoundedCornerShape(16.dp),
                height = 58.dp,
                isEnabled = false,
                disabledColor = BorderDisabled,
                textColor = TextDisabled,
                textStyle = Typography.bodyLargeB,
                onClick = {}
            )
        }
    }
}

@Composable
private fun StartConfirmDialog(
    isShow: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DarkConfirmDialog(
        isShow = isShow,
        titleRes = R.string.vote_start_confirm_title,
        contentRes = R.string.vote_start_confirm_content,
        actionRes = R.string.vote_confirm_start,
        actionColor = ButtonEnabled,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
private fun EndConfirmDialog(
    isShow: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DarkConfirmDialog(
        isShow = isShow,
        titleRes = R.string.vote_end_confirm_title,
        contentRes = R.string.vote_end_confirm_content,
        actionRes = R.string.vote_confirm_end,
        actionColor = VoteEndRed,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
private fun DarkConfirmDialog(
    isShow: Boolean,
    titleRes: Int,
    contentRes: Int,
    actionRes: Int,
    actionColor: Color,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isShow) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(BackgroundSecondaryDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DddText(
                    text = stringResource(titleRes),
                    style = Typography.titleSmallB,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                DddText(
                    text = stringResource(contentRes),
                    style = Typography.bodySmallR,
                    color = TextSecondaryDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DddLargeSizeButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.vote_confirm_cancel),
                        shape = RoundedCornerShape(12.dp),
                        height = 48.dp,
                        enabledColor = BackgroundDefault,
                        textStyle = Typography.bodyMediumM,
                        onClick = onDismiss
                    )

                    DddLargeSizeButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(actionRes),
                        shape = RoundedCornerShape(12.dp),
                        height = 48.dp,
                        enabledColor = actionColor,
                        textStyle = Typography.bodyMediumM,
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@Composable
private fun NotParticipatedDialog(
    isShow: Boolean,
    members: ImmutableList<VoteMember>,
    onDismiss: () -> Unit
) {
    if (!isShow) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(BackgroundSecondaryDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                DddText(
                    text = stringResource(R.string.vote_not_participated_title),
                    style = Typography.titleSmallB,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                DddText(
                    text = stringResource(
                        R.string.vote_not_participated_subtitle,
                        members.size
                    ),
                    style = Typography.bodySmallR,
                    color = TextSecondaryDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 420.dp)
                ) {
                    items(members) { member ->
                        Column {
                            NotParticipatedMemberRow(member = member)

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(BorderDisabled)
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                DddLargeSizeButton(
                    text = stringResource(R.string.vote_not_participated_close),
                    shape = RoundedCornerShape(16.dp),
                    height = 52.dp,
                    textStyle = Typography.bodyLargeB,
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun NotParticipatedMemberRow(
    member: VoteMember
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DddText(
            text = member.name,
            style = Typography.bodyMediumM,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.size(8.dp))

        TeamChip(team = member.team)

        Spacer(modifier = Modifier.weight(1f))

        AttendanceStatusChip(status = member.attendanceStatus)
    }
}

@Composable
private fun TeamChip(team: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(BorderDisabled)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        DddText(
            text = team,
            style = Typography.bodySmallM,
            color = TextSecondaryDark
        )
    }
}

@Composable
private fun AttendanceStatusChip(status: VoteAttendanceStatus) {
    val (label, bg, text) = when (status) {
        VoteAttendanceStatus.ATTENDED -> Triple(R.string.vote_attendance_attended, AttendedBg, AttendedText)
        VoteAttendanceStatus.LATE -> Triple(R.string.vote_attendance_late, LateBg, LateText)
        VoteAttendanceStatus.ABSENT -> Triple(R.string.vote_attendance_absent, AbsentBg, AbsentText)
        VoteAttendanceStatus.NONE -> Triple(R.string.vote_attendance_none, NoneBg, NoneText)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        DddText(
            text = stringResource(label),
            style = Typography.bodySmallM,
            color = text
        )
    }
}
