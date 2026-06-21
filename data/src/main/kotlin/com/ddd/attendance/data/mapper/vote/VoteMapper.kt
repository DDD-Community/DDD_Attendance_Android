package com.ddd.attendance.data.mapper.vote

import com.ddd.attendance.data.model.vote.ActiveVoteResponse
import com.ddd.attendance.data.model.vote.CategoryResponse
import com.ddd.attendance.data.model.vote.FeedbackQuestionAnswerRequest
import com.ddd.attendance.data.model.vote.FeedbackResultOptionResponse
import com.ddd.attendance.data.model.vote.FeedbackResultQuestionResponse
import com.ddd.attendance.data.model.vote.FeedbackResultsResponse
import com.ddd.attendance.data.model.vote.FeedbackTemplateBody
import com.ddd.attendance.data.model.vote.FeedbackTemplateResponse
import com.ddd.attendance.data.model.vote.MyVoteStatusResponse
import com.ddd.attendance.data.model.vote.NonResponderResponse
import com.ddd.attendance.data.model.vote.OptionResponse
import com.ddd.attendance.data.model.vote.QuestionResponse
import com.ddd.attendance.data.model.vote.TeamItemResponse
import com.ddd.attendance.data.model.vote.TeamVoteCategoryAnswerRequest
import com.ddd.attendance.data.model.vote.TeamVoteResultCategoryResponse
import com.ddd.attendance.data.model.vote.TeamVoteResultTeamResponse
import com.ddd.attendance.data.model.vote.TeamVoteResultsResponse
import com.ddd.attendance.data.model.vote.TeamVoteTemplateBody
import com.ddd.attendance.data.model.vote.TeamVoteTemplateResponse
import com.ddd.attendance.data.model.vote.VoteDetailResponse
import com.ddd.attendance.data.model.vote.VoteNonRespondersResponse
import com.ddd.attendance.data.model.vote.VoteParticipationResponse
import com.ddd.attendance.data.model.vote.VoteSubmitRequest
import com.ddd.attendance.data.model.vote.VoteSummaryResponse
import com.ddd.attendance.domain.model.vote.ActiveVote
import com.ddd.attendance.domain.model.vote.FeedbackOption
import com.ddd.attendance.domain.model.vote.FeedbackQuestion
import com.ddd.attendance.domain.model.vote.FeedbackQuestionType
import com.ddd.attendance.domain.model.vote.FeedbackResultOption
import com.ddd.attendance.domain.model.vote.FeedbackResultQuestion
import com.ddd.attendance.domain.model.vote.FeedbackResults
import com.ddd.attendance.domain.model.vote.FeedbackTemplate
import com.ddd.attendance.domain.model.vote.MyVoteStatus
import com.ddd.attendance.domain.model.vote.NonResponder
import com.ddd.attendance.domain.model.vote.TeamVoteResultCategory
import com.ddd.attendance.domain.model.vote.TeamVoteResultTeam
import com.ddd.attendance.domain.model.vote.TeamVoteResults
import com.ddd.attendance.domain.model.vote.TeamVoteTemplate
import com.ddd.attendance.domain.model.vote.TodayAttendanceStatus
import com.ddd.attendance.domain.model.vote.VoteCategory
import com.ddd.attendance.domain.model.vote.VoteDetail
import com.ddd.attendance.domain.model.vote.VoteDetailFeedbackTemplate
import com.ddd.attendance.domain.model.vote.VoteDetailTeamTemplate
import com.ddd.attendance.domain.model.vote.VoteNonResponders
import com.ddd.attendance.domain.model.vote.VoteParticipation
import com.ddd.attendance.domain.model.vote.VoteStatus
import com.ddd.attendance.domain.model.vote.VoteSubmission
import com.ddd.attendance.domain.model.vote.VoteSummary
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

fun VoteSummaryResponse.toDomain(): VoteSummary =
    VoteSummary(
        voteId = voteId,
        title = title,
        status = status.toVoteStatus(),
        openedAt = openedAt,
        closedAt = closedAt,
        createdDate = createdDate
    )

fun VoteParticipationResponse.toDomain(): VoteParticipation =
    VoteParticipation(
        voteId = voteId,
        title = title,
        status = status.toVoteStatus(),
        totalMembers = totalMembers,
        respondedMembers = respondedMembers,
        participationRate = participationRate
    )

fun VoteNonRespondersResponse.toDomain(): VoteNonResponders =
    VoteNonResponders(
        totalCount = totalCount,
        members = members.map { it.toDomain() }
    )

private fun NonResponderResponse.toDomain(): NonResponder =
    NonResponder(
        memberId = memberId,
        name = name,
        teamName = teamName,
        todayAttendanceStatus = todayAttendanceStatus.toTodayAttendanceStatus()
    )

private fun String.toTodayAttendanceStatus(): TodayAttendanceStatus =
    runCatching { TodayAttendanceStatus.valueOf(this) }.getOrDefault(TodayAttendanceStatus.NONE)

fun VoteDetailResponse.toDomain(): VoteDetail =
    VoteDetail(
        voteId = voteId,
        title = title,
        status = status.toVoteStatus(),
        templateVersion = templateVersion,
        teamVoteTemplate = teamVoteTemplate?.toDomain(),
        feedbackTemplate = feedbackTemplate?.toDomain()
    )

private fun TeamVoteTemplateBody.toDomain(): VoteDetailTeamTemplate =
    VoteDetailTeamTemplate(
        title = title,
        description = description,
        notice = notice,
        categories = categories.sortedBy { it.order }.map { it.toDomain() }
    )

private fun FeedbackTemplateBody.toDomain(): VoteDetailFeedbackTemplate =
    VoteDetailFeedbackTemplate(
        title = title,
        description = description,
        questions = questions.sortedBy { it.order }.map { it.toDomain() }
    )

fun TeamVoteResultsResponse.toDomain(): TeamVoteResults =
    TeamVoteResults(
        voteId = voteId,
        title = title,
        status = status.toVoteStatus(),
        totalResponses = totalResponses,
        categories = categories.sortedBy { it.order }.map { it.toDomain() }
    )

private fun TeamVoteResultCategoryResponse.toDomain(): TeamVoteResultCategory =
    TeamVoteResultCategory(
        categoryId = categoryId,
        title = title,
        order = order,
        teams = teams.sortedBy { it.rank }.map { it.toDomain() },
        reasons = reasons
    )

private fun TeamVoteResultTeamResponse.toDomain(): TeamVoteResultTeam =
    TeamVoteResultTeam(
        rank = rank,
        teamId = teamId,
        name = name,
        serviceName = serviceName,
        voteCount = voteCount
    )

fun FeedbackResultsResponse.toDomain(): FeedbackResults =
    FeedbackResults(
        voteId = voteId,
        totalResponses = totalResponses,
        questions = questions.sortedBy { it.order }.map { it.toDomain() }
    )

private fun FeedbackResultQuestionResponse.toDomain(): FeedbackResultQuestion =
    FeedbackResultQuestion(
        questionId = questionId,
        title = title,
        type = type.toQuestionType(),
        order = order,
        options = options?.map { it.toDomain() } ?: emptyList(),
        trueCount = trueCount,
        falseCount = falseCount,
        textAnswers = textAnswers ?: emptyList()
    )

private fun FeedbackResultOptionResponse.toDomain(): FeedbackResultOption =
    FeedbackResultOption(
        optionId = optionId,
        label = label,
        count = count
    )

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
