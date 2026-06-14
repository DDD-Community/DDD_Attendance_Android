package com.ddd.attendance.feature.member.vote.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ddd.attendance.domain.model.vote.FeedbackQuestionType
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.member.vote.VoteIntent
import com.ddd.attendance.feature.member.vote.VoteUiState
import com.ddd.attendance.feature.member.vote.component.ChoiceChip
import com.ddd.attendance.feature.member.vote.component.LimitedTextField
import com.ddd.attendance.feature.member.vote.component.VoteStepProgress
import com.ddd.attendance.feature.member.vote.component.VoteTopBar
import com.ddd.attendance.feature.member.vote.component.YesNoToggle
import com.ddd.attendance.feature.member.vote.model.FeedbackQuestionUiModel

@Composable
internal fun Step2FeedbackScreen(
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
            VoteStepProgress(current = 2, total = 2)
            Spacer(modifier = Modifier.height(20.dp))

            DddText(text = uiState.feedbackTitle, style = Typography.headlineSmallB, color = TextPrimary)
            if (uiState.feedbackDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                DddText(text = uiState.feedbackDescription, style = Typography.bodySmallM, color = TextSecondaryDark)
            }

            uiState.questions.forEachIndexed { index, question ->
                Spacer(modifier = Modifier.height(28.dp))
                QuestionSection(
                    label = "${index + 1}. ${question.title}",
                    question = question,
                    uiState = uiState,
                    onIntent = onIntent
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Column(modifier = Modifier.padding(20.dp)) {
            DddLargeSizeButton(
                text = "제출하기",
                isEnabled = uiState.isStep2Valid && !uiState.isSubmitting,
                onClick = { onIntent(VoteIntent.Submit) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuestionSection(
    label: String,
    question: FeedbackQuestionUiModel,
    uiState: VoteUiState,
    onIntent: (VoteIntent) -> Unit
) {
    DddText(text = label, style = Typography.titleSmallB, color = TextPrimary)
    if (!question.helpText.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        DddText(text = question.helpText, style = Typography.bodySmallR, color = TextSecondaryDark)
    }
    Spacer(modifier = Modifier.height(12.dp))

    when (question.type) {
        FeedbackQuestionType.MULTI_SELECT -> {
            val selected = uiState.optionAnswers[question.id].orEmpty()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                question.options.forEach { option ->
                    ChoiceChip(
                        label = option.label,
                        isSelected = option.id in selected,
                        onClick = { onIntent(VoteIntent.OptionToggled(question.id, option.id)) }
                    )
                }
            }
            question.followUp?.let { followUp ->
                Spacer(modifier = Modifier.height(16.dp))
                LongTextQuestion(question = followUp, uiState = uiState, onIntent = onIntent, label = followUp.title)
            }
        }

        FeedbackQuestionType.LONG_TEXT ->
            LongTextQuestion(question = question, uiState = uiState, onIntent = onIntent, label = null)

        FeedbackQuestionType.BOOLEAN -> {
            YesNoToggle(
                selected = uiState.boolAnswers[question.id],
                onSelected = { onIntent(VoteIntent.BoolSelected(question.id, it)) }
            )
        }

        else -> Unit
    }
}

@Composable
private fun LongTextQuestion(
    question: FeedbackQuestionUiModel,
    uiState: VoteUiState,
    onIntent: (VoteIntent) -> Unit,
    label: String?
) {
    if (!label.isNullOrBlank()) {
        DddText(text = label, style = Typography.bodyMediumM, color = TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
    }
    val text = uiState.textAnswers[question.id].orEmpty()
    LimitedTextField(
        value = text,
        onValueChange = { onIntent(VoteIntent.TextChanged(question.id, it)) },
        placeholder = "자유롭게 의견을 남겨주세요.",
        maxLength = question.maxLength ?: 300,
        helperText = question.helpText?.takeIf { label != null }
    )
}
