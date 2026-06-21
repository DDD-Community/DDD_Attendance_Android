package com.ddd.attendance.feature.admin.vote.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.admin.vote.R
import com.ddd.attendance.feature.admin.vote.VoteResultsTab
import com.ddd.attendance.feature.admin.vote.model.FeedbackResultOptionUi
import com.ddd.attendance.feature.admin.vote.model.FeedbackResultQuestionUi
import com.ddd.attendance.feature.admin.vote.model.FeedbackResultsUi
import com.ddd.attendance.feature.admin.vote.model.TeamVoteResultCategoryUi
import com.ddd.attendance.feature.admin.vote.model.TeamVoteResultTeamUi
import com.ddd.attendance.feature.admin.vote.model.TeamVoteResultsUi
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
internal fun VoteResultsContent(
    modifier: Modifier = Modifier,
    teamResults: TeamVoteResultsUi?,
    feedbackResults: FeedbackResultsUi?,
    selectedTab: VoteResultsTab,
    isLoading: Boolean,
    onSelectTab: (VoteResultsTab) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        DddText(
            text = stringResource(R.string.vote_results_title),
            style = Typography.titleLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        val totalResponses = when (selectedTab) {
            VoteResultsTab.TeamVote -> teamResults?.totalResponses
            VoteResultsTab.Feedback -> feedbackResults?.totalResponses
        }
        if (totalResponses != null) {
            DddText(
                text = stringResource(R.string.vote_results_total, totalResponses),
                style = Typography.bodySmallR,
                color = TextDisabled
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selected = selectedTab, onSelect = onSelectTab)

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading -> CenterText("...")
                selectedTab == VoteResultsTab.TeamVote ->
                    TeamResultsList(teamResults)
                selectedTab == VoteResultsTab.Feedback ->
                    FeedbackResultsList(feedbackResults)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DddLargeSizeButton(
            text = stringResource(R.string.vote_list_back),
            shape = RoundedCornerShape(16.dp),
            height = 58.dp,
            textStyle = Typography.bodyLargeB,
            onClick = onBack
        )
    }
}

@Composable
private fun TabRow(
    selected: VoteResultsTab,
    onSelect: (VoteResultsTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BackgroundSecondaryDark)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TabItem(
            label = stringResource(R.string.vote_results_tab_team),
            selected = selected == VoteResultsTab.TeamVote,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(VoteResultsTab.TeamVote) }
        )
        TabItem(
            label = stringResource(R.string.vote_results_tab_feedback),
            selected = selected == VoteResultsTab.Feedback,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(VoteResultsTab.Feedback) }
        )
    }
}

@Composable
private fun TabItem(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) ButtonEnabled else BackgroundSecondaryDark)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        DddText(
            text = label,
            style = Typography.bodyMediumM,
            color = if (selected) TextPrimary else TextSecondaryDark
        )
    }
}

@Composable
private fun TeamResultsList(results: TeamVoteResultsUi?) {
    if (results == null || results.categories.isEmpty()) {
        CenterText(stringResource(R.string.vote_results_empty))
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(results.categories.size, key = { results.categories[it].categoryId }) { idx ->
            TeamCategoryCard(results.categories[idx])
        }
    }
}

@Composable
private fun TeamCategoryCard(category: TeamVoteResultCategoryUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondaryDark)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        DddText(
            text = category.title,
            style = Typography.bodyLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (category.teams.isEmpty()) {
            DddText(
                text = stringResource(R.string.vote_results_empty),
                style = Typography.bodySmallR,
                color = TextDisabled
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                category.teams.forEach { TeamResultRow(it) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DddText(
            text = stringResource(R.string.vote_results_reasons_title),
            style = Typography.bodyMediumM,
            color = TextSecondaryDark
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (category.reasons.isEmpty()) {
            DddText(
                text = stringResource(R.string.vote_results_reasons_empty),
                style = Typography.bodySmallR,
                color = TextDisabled
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                category.reasons.forEach { reason ->
                    DddText(
                        text = "· $reason",
                        style = Typography.bodySmallR,
                        color = TextSecondaryDark
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamResultRow(team: TeamVoteResultTeamUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BorderDisabled)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DddText(
            text = stringResource(R.string.vote_results_rank, team.rank),
            style = Typography.bodyMediumM,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        Column(modifier = Modifier.weight(1f)) {
            DddText(
                text = team.name,
                style = Typography.bodyMediumM,
                color = TextPrimary
            )
            if (!team.serviceName.isNullOrBlank()) {
                DddText(
                    text = team.serviceName,
                    style = Typography.bodySmallR,
                    color = TextSecondaryDark
                )
            }
        }
        DddText(
            text = stringResource(R.string.vote_results_votes, team.voteCount),
            style = Typography.bodyMediumM,
            color = TextPrimary
        )
    }
}

@Composable
private fun FeedbackResultsList(results: FeedbackResultsUi?) {
    if (results == null || results.questions.isEmpty()) {
        CenterText(stringResource(R.string.vote_results_empty))
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(results.questions.size, key = { results.questions[it].questionId }) { idx ->
            FeedbackQuestionCard(results.questions[idx])
        }
    }
}

@Composable
private fun FeedbackQuestionCard(question: FeedbackResultQuestionUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondaryDark)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        DddText(
            text = question.title,
            style = Typography.bodyLargeB,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        when (question) {
            is FeedbackResultQuestionUi.MultiSelect -> MultiSelectBody(question.options)
            is FeedbackResultQuestionUi.Boolean -> BooleanBody(question.trueCount, question.falseCount)
            is FeedbackResultQuestionUi.LongText -> LongTextBody(question.answers)
            is FeedbackResultQuestionUi.Unknown -> DddText(
                text = stringResource(R.string.vote_results_empty),
                style = Typography.bodySmallR,
                color = TextDisabled
            )
        }
    }
}

@Composable
private fun MultiSelectBody(options: List<FeedbackResultOptionUi>) {
    if (options.isEmpty()) {
        DddText(
            text = stringResource(R.string.vote_results_empty),
            style = Typography.bodySmallR,
            color = TextDisabled
        )
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BorderDisabled)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DddText(
                    text = option.label,
                    style = Typography.bodyMediumM,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                DddText(
                    text = stringResource(R.string.vote_results_votes, option.count),
                    style = Typography.bodyMediumM,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun BooleanBody(trueCount: Int, falseCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(BorderDisabled)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            DddText(
                text = stringResource(R.string.vote_results_boolean_yes, trueCount),
                style = Typography.bodyMediumM,
                color = TextPrimary
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(BorderDisabled)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            DddText(
                text = stringResource(R.string.vote_results_boolean_no, falseCount),
                style = Typography.bodyMediumM,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun LongTextBody(answers: List<String>) {
    if (answers.isEmpty()) {
        DddText(
            text = stringResource(R.string.vote_results_long_text_empty),
            style = Typography.bodySmallR,
            color = TextDisabled
        )
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        answers.forEach { answer ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BorderDisabled)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                DddText(
                    text = answer,
                    style = Typography.bodySmallR,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun CenterText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DddText(
            text = text,
            style = Typography.bodyMediumM,
            color = TextDisabled
        )
    }
}
