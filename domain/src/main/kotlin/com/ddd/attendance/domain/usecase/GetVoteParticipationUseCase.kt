package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.VoteParticipation
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVoteParticipationUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(voteId: Int): Flow<VoteParticipation> =
        voteRepository.getVoteParticipation(voteId)
}
