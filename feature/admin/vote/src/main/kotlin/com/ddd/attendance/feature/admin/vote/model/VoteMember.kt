package com.ddd.attendance.feature.admin.vote.model

data class VoteMember(
    val name: String,
    val team: String,
    val attendanceStatus: VoteAttendanceStatus
)

enum class VoteAttendanceStatus {
    ATTENDED, LATE, ABSENT, NONE
}

data class VoteListItemUi(
    val voteId: Int,
    val title: String,
    val statusLabel: String,
    val statusKind: VoteStatusKind,
    val createdDate: String
)

enum class VoteStatusKind {
    DRAFT, OPEN, CLOSED, UNKNOWN
}

data class VoteDetailUi(
    val voteId: Int,
    val title: String,
    val statusLabel: String,
    val statusKind: VoteStatusKind,
    val teamTemplate: VoteDetailTeamTemplateUi?,
    val feedbackTemplate: VoteDetailFeedbackTemplateUi?
)

data class VoteDetailTeamTemplateUi(
    val title: String,
    val description: String,
    val notice: String,
    val categories: List<VoteDetailCategoryUi>
)

data class VoteDetailCategoryUi(
    val id: String,
    val title: String,
    val maxSelectableTeams: Int,
    val reasonRequired: Boolean
)

data class VoteDetailFeedbackTemplateUi(
    val title: String,
    val description: String,
    val questions: List<VoteDetailQuestionUi>
)

data class VoteDetailQuestionUi(
    val id: String,
    val type: String,
    val title: String,
    val required: Boolean,
    val options: List<String>
)

data class TeamVoteResultsUi(
    val totalResponses: Int,
    val categories: List<TeamVoteResultCategoryUi>
)

data class TeamVoteResultCategoryUi(
    val categoryId: String,
    val title: String,
    val teams: List<TeamVoteResultTeamUi>,
    val reasons: List<String>
)

data class TeamVoteResultTeamUi(
    val rank: Int,
    val name: String,
    val serviceName: String?,
    val voteCount: Int
)

data class FeedbackResultsUi(
    val totalResponses: Int,
    val questions: List<FeedbackResultQuestionUi>
)

sealed interface FeedbackResultQuestionUi {
    val questionId: String
    val title: String

    data class MultiSelect(
        override val questionId: String,
        override val title: String,
        val options: List<FeedbackResultOptionUi>
    ) : FeedbackResultQuestionUi

    data class Boolean(
        override val questionId: String,
        override val title: String,
        val trueCount: Int,
        val falseCount: Int
    ) : FeedbackResultQuestionUi

    data class LongText(
        override val questionId: String,
        override val title: String,
        val answers: List<String>
    ) : FeedbackResultQuestionUi

    data class Unknown(
        override val questionId: String,
        override val title: String
    ) : FeedbackResultQuestionUi
}

data class FeedbackResultOptionUi(
    val optionId: String,
    val label: String,
    val count: Int
)
