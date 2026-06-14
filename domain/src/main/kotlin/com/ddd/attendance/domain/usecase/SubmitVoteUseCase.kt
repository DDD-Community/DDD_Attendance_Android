package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.VoteSubmission
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubmitVoteUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(voteId: Int, submission: VoteSubmission): Flow<Unit> =
        voteRepository.submitVote(voteId, submission)
}
