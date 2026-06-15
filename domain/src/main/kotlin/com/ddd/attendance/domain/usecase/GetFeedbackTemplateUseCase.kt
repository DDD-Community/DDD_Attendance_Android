package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.FeedbackTemplate
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeedbackTemplateUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(voteId: Int): Flow<FeedbackTemplate> =
        voteRepository.getFeedbackTemplate(voteId)
}
