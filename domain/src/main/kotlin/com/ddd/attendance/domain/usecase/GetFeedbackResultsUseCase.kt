package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.FeedbackResults
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeedbackResultsUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(voteId: Int): Flow<FeedbackResults> =
        voteRepository.getFeedbackResults(voteId)
}
