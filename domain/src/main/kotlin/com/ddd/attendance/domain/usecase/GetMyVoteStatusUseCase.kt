package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.MyVoteStatus
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyVoteStatusUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(voteId: Int): Flow<MyVoteStatus> =
        voteRepository.getMyVoteStatus(voteId)
}
