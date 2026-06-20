package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.VoteSummary
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVotesUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(): Flow<List<VoteSummary>> = voteRepository.getVotes()
}
