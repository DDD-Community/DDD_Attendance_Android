package com.ddd.attendance.feature.admin.vote.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.admin.vote.R
import com.ddd.attendance.feature.admin.vote.model.VoteDetailCategoryUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailFeedbackTemplateUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailQuestionUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailTeamTemplateUi
import com.ddd.attendance.feature.admin.vote.model.VoteDetailUi
import com.ddd.attendance.feature.admin.vote.model.VoteStatusKind
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
internal fun VoteDetailContent(
    modifier: Modifier = Modifier,
    detail: VoteDetailUi?,
    isLoading: Boolean,
    onOpenResults: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        DddText(
            text = stringResource(R.string.vote_detail_title),
            style = Typography.titleLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading || detail == null -> {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DddText(
                        text = "...",
                        style = Typography.bodyMediumM,
                        color = TextDisabled
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { DetailHeader(detail) }
                    item { TeamTemplateSection(detail.teamTemplate) }
                    item { FeedbackTemplateSection(detail.feedbackTemplate) }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DddLargeSizeButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.vote_list_back),
                shape = RoundedCornerShape(16.dp),
                height = 58.dp,
                enabledColor = BorderDisabled,
                textStyle = Typography.bodyLargeB,
                onClick = onBack
            )
            val results = detail?.voteId
            if (results != null && detail.statusKind != VoteStatusKind.DRAFT) {
                DddLargeSizeButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.vote_detail_open_results),
                    shape = RoundedCornerShape(16.dp),
                    height = 58.dp,
                    enabledColor = ButtonEnabled,
                    textStyle = Typography.bodyLargeB,
                    onClick = { onOpenResults(results) }
                )
            }
        }
    }
}

@Composable
private fun DetailHeader(detail: VoteDetailUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondaryDark)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DddText(
                text = detail.title.ifBlank { "제목 없음" },
                style = Typography.bodyLargeB,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(BorderDisabled)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                DddText(
                    text = detail.statusLabel,
                    style = Typography.bodySmallM,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun TeamTemplateSection(template: VoteDetailTeamTemplateUi?) {
    SectionCard(title = stringResource(R.string.vote_detail_section_team_vote)) {
        if (template == null) {
            EmptyText()
            return@SectionCard
        }
        if (template.description.isNotBlank()) {
            DddText(
                text = template.description,
                style = Typography.bodySmallR,
                color = TextSecondaryDark
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (template.categories.isEmpty()) {
            EmptyText()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                template.categories.forEach { CategoryRow(it) }
            }
        }
    }
}

@Composable
private fun CategoryRow(category: VoteDetailCategoryUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BorderDisabled)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        DddText(
            text = category.title,
            style = Typography.bodyMediumM,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        DddText(
            text = stringResource(
                R.string.vote_detail_category_meta,
                category.maxSelectableTeams
            ) + " · " + stringResource(
                if (category.reasonRequired) R.string.vote_detail_reason_required
                else R.string.vote_detail_reason_optional
            ),
            style = Typography.bodySmallR,
            color = TextSecondaryDark
        )
    }
}

@Composable
private fun FeedbackTemplateSection(template: VoteDetailFeedbackTemplateUi?) {
    SectionCard(title = stringResource(R.string.vote_detail_section_feedback)) {
        if (template == null) {
            EmptyText()
            return@SectionCard
        }
        if (template.description.isNotBlank()) {
            DddText(
                text = template.description,
                style = Typography.bodySmallR,
                color = TextSecondaryDark
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (template.questions.isEmpty()) {
            EmptyText()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                template.questions.forEach { QuestionRow(it) }
            }
        }
    }
}

@Composable
private fun QuestionRow(question: VoteDetailQuestionUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BorderDisabled)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DddText(
                text = question.title,
                style = Typography.bodyMediumM,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            DddText(
                text = stringResource(
                    if (question.required) R.string.vote_detail_required
                    else R.string.vote_detail_optional
                ),
                style = Typography.bodySmallM,
                color = TextSecondaryDark
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        DddText(
            text = question.type,
            style = Typography.bodySmallR,
            color = TextSecondaryDark
        )
        if (question.options.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            question.options.forEach { label ->
                DddText(
                    text = "· $label",
                    style = Typography.bodySmallR,
                    color = TextSecondaryDark
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondaryDark)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        DddText(
            text = title,
            style = Typography.bodyLargeB,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun EmptyText() {
    DddText(
        text = stringResource(R.string.vote_detail_section_empty),
        style = Typography.bodySmallR,
        color = TextDisabled
    )
}
