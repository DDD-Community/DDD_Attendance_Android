package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.VoteDetail
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVoteDetailUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(voteId: Int): Flow<VoteDetail> = voteRepository.getVoteDetail(voteId)
}
