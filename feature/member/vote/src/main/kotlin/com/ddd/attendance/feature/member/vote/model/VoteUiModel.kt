package com.ddd.attendance.feature.member.vote.model

import com.ddd.attendance.domain.model.vote.FeedbackQuestionType

/**
 * STEP 1 팀 투표 부문(템플릿). 사용자 응답(선택 팀/사유)은 VoteUiState 의 map 에 분리 보관한다.
 */
data class CategoryUiModel(
    val id: String,
    val title: String,
    val maxSelectableTeams: Int,
    val reasonLabel: String,
    val reasonRequired: Boolean,
    val reasonMinLength: Int,
    val reasonMaxLength: Int
)

data class TeamUiModel(
    val teamId: Int,
    val name: String,
    val serviceName: String?,
    val isOwnTeam: Boolean
)

/**
 * STEP 2 피드백 질문(템플릿). 타입별로 동적 렌더링한다.
 */
data class FeedbackQuestionUiModel(
    val id: String,
    val type: FeedbackQuestionType,
    val title: String,
    val helpText: String?,
    val required: Boolean,
    val maxSelectableOptions: Int?,
    val maxLength: Int?,
    val options: List<OptionUiModel>,
    val followUp: FeedbackQuestionUiModel?
)

data class OptionUiModel(
    val id: String,
    val label: String
)
