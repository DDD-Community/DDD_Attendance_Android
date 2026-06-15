package com.ddd.attendance.feature.member.vote.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.member.vote.VoteIntent
import com.ddd.attendance.feature.member.vote.VoteUiState
import com.ddd.attendance.feature.member.vote.component.LimitedTextField
import com.ddd.attendance.feature.member.vote.component.TeamSelectRow
import com.ddd.attendance.feature.member.vote.component.VoteStepProgress
import com.ddd.attendance.feature.member.vote.component.VoteTopBar
import com.ddd.attendance.feature.member.vote.model.CategoryUiModel

@Composable
internal fun Step1TeamVoteScreen(
    uiState: VoteUiState,
    onIntent: (VoteIntent) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        VoteTopBar(onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            VoteStepProgress(current = 1, total = 2)
            Spacer(modifier = Modifier.height(20.dp))

            DddText(text = uiState.teamVoteTitle, style = Typography.headlineSmallB, color = TextPrimary)
            if (uiState.teamVoteDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                DddText(text = uiState.teamVoteDescription, style = Typography.bodySmallM, color = TextDisabled)
            }
            if (uiState.teamVoteNotice.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                DddText(text = uiState.teamVoteNotice, style = Typography.bodySmallR, color = TextDisabled)
            }

            uiState.categories.forEachIndexed { index, category ->
                Spacer(modifier = Modifier.height(28.dp))
                CategorySection(
                    index = index + 1,
                    category = category,
                    uiState = uiState,
                    onIntent = onIntent
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Column(modifier = Modifier.padding(20.dp)) {
            DddLargeSizeButton(
                text = "다음",
                isEnabled = uiState.isStep1Valid,
                onClick = { onIntent(VoteIntent.GoToStep2) }
            )
        }
    }
}

@Composable
private fun CategorySection(
    index: Int,
    category: CategoryUiModel,
    uiState: VoteUiState,
    onIntent: (VoteIntent) -> Unit
) {
    val selected = uiState.teamSelections[category.id].orEmpty()
    val reason = uiState.reasons[category.id].orEmpty()
    val reasonError = reason.isNotEmpty() && reason.length < category.reasonMinLength

    DddText(
        text = "$index. ${category.title}가 좋았던 팀을 선택해주세요.",
        style = Typography.titleSmallB,
        color = TextPrimary,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DddText(
            text = "각 부문 최대 ${category.maxSelectableTeams}팀 선택",
            style = Typography.bodySmallR,
            color = TextSecondaryDark
        )
        DddText(
            text = "${selected.size} / ${category.maxSelectableTeams}",
            style = Typography.bodySmallB,
            color = ButtonEnabled
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    uiState.teams.forEach { team ->
        TeamSelectRow(
            team = team,
            isSelected = team.teamId in selected,
            onClick = { onIntent(VoteIntent.TeamToggled(category.id, team.teamId)) }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    DddText(text = category.reasonLabel, style = Typography.bodyMediumM, color = TextPrimary)
    Spacer(modifier = Modifier.height(8.dp))
    LimitedTextField(
        value = reason,
        onValueChange = { onIntent(VoteIntent.ReasonChanged(category.id, it)) },
        placeholder = "해당 팀을 선택하신 이유를 적어주세요.",
        maxLength = category.reasonMaxLength,
        isError = reasonError,
        helperText = if (category.reasonMinLength > 0) "최소 ${category.reasonMinLength}자 이상 입력해주세요." else null
    )
}
