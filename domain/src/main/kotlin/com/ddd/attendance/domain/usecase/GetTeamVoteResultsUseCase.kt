package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.TeamVoteResults
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamVoteResultsUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(voteId: Int): Flow<TeamVoteResults> =
        voteRepository.getTeamVoteResults(voteId)
}
