package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.vote.ActiveVote
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveVoteUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    operator fun invoke(): Flow<ActiveVote?> = voteRepository.getActiveVote()
}
