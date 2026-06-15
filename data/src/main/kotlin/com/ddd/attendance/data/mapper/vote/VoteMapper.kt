package com.ddd.attendance.data.mapper.vote

import com.ddd.attendance.data.model.vote.ActiveVoteResponse
import com.ddd.attendance.data.model.vote.CategoryResponse
import com.ddd.attendance.data.model.vote.FeedbackQuestionAnswerRequest
import com.ddd.attendance.data.model.vote.FeedbackTemplateResponse
import com.ddd.attendance.data.model.vote.MyVoteStatusResponse
import com.ddd.attendance.data.model.vote.OptionResponse
import com.ddd.attendance.data.model.vote.QuestionResponse
import com.ddd.attendance.data.model.vote.TeamItemResponse
import com.ddd.attendance.data.model.vote.TeamVoteCategoryAnswerRequest
import com.ddd.attendance.data.model.vote.TeamVoteTemplateResponse
import com.ddd.attendance.data.model.vote.VoteSubmitRequest
import com.ddd.attendance.domain.model.vote.ActiveVote
import com.ddd.attendance.domain.model.vote.FeedbackOption
import com.ddd.attendance.domain.model.vote.FeedbackQuestion
import com.ddd.attendance.domain.model.vote.FeedbackQuestionType
import com.ddd.attendance.domain.model.vote.FeedbackTemplate
import com.ddd.attendance.domain.model.vote.MyVoteStatus
import com.ddd.attendance.domain.model.vote.TeamVoteTemplate
import com.ddd.attendance.domain.model.vote.VoteCategory
import com.ddd.attendance.domain.model.vote.VoteStatus
import com.ddd.attendance.domain.model.vote.VoteSubmission
import com.ddd.attendance.domain.model.vote.VoteTeam

fun ActiveVoteResponse.toDomain(): ActiveVote? {
    val id = voteId ?: return null
    return ActiveVote(
        voteId = id,
        title = title.orEmpty(),
        alreadyResponded = alreadyResponded
    )
}

fun MyVoteStatusResponse.toDomain(): MyVoteStatus =
    MyVoteStatus(voteId = voteId, responded = responded)

private fun String.toVoteStatus(): VoteStatus =
    runCatching { VoteStatus.valueOf(this) }.getOrDefault(VoteStatus.UNKNOWN)

fun TeamVoteTemplateResponse.toDomain(): TeamVoteTemplate =
    TeamVoteTemplate(
        status = status.toVoteStatus(),
        title = template.title,
        description = template.description,
        notice = template.notice,
        categories = template.categories.sortedBy { it.order }.map { it.toDomain() },
        teams = teams.map { it.toDomain() }
    )

private fun CategoryResponse.toDomain(): VoteCategory =
    VoteCategory(
        id = id,
        order = order,
        title = title,
        maxSelectableTeams = maxSelectableTeams,
        reasonRequired = reasonRequired,
        reasonMinLength = reasonMinLength,
        reasonMaxLength = reasonMaxLength,
        reasonLabel = reasonLabel
    )

private fun TeamItemResponse.toDomain(): VoteTeam =
    VoteTeam(
        teamId = teamId,
        name = name,
        serviceName = serviceName,
        isOwnTeam = isOwnTeam
    )

fun FeedbackTemplateResponse.toDomain(): FeedbackTemplate =
    FeedbackTemplate(
        status = status.toVoteStatus(),
        title = template.title,
        description = template.description,
        questions = template.questions.sortedBy { it.order }.map { it.toDomain() }
    )

private fun QuestionResponse.toDomain(): FeedbackQuestion =
    FeedbackQuestion(
        id = id,
        order = order,
        type = type.toQuestionType(),
        title = title,
        helpText = helpText,
        required = required,
        maxSelectableOptions = maxSelectableOptions,
        maxLength = maxLength,
        options = options?.map { it.toDomain() } ?: emptyList(),
        followUp = followUp?.toDomain()
    )

private fun OptionResponse.toDomain(): FeedbackOption =
    FeedbackOption(id = id, label = label)

private fun String.toQuestionType(): FeedbackQuestionType =
    runCatching { FeedbackQuestionType.valueOf(this) }.getOrDefault(FeedbackQuestionType.UNKNOWN)

fun VoteSubmission.toRequest(): VoteSubmitRequest =
    VoteSubmitRequest(
        teamVote = teamVote.map {
            TeamVoteCategoryAnswerRequest(
                categoryId = it.categoryId,
                teamIds = it.teamIds,
                reason = it.reason
            )
        },
        feedback = feedback.map {
            FeedbackQuestionAnswerRequest(
                questionId = it.questionId,
                optionIds = it.optionIds,
                textValue = it.textValue,
                boolValue = it.boolValue
            )
        }
    )
