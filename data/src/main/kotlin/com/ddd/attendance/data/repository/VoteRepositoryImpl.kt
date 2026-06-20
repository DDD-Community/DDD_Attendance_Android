package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiVoteDataSource
import com.ddd.attendance.data.mapper.vote.toDomain
import com.ddd.attendance.data.mapper.vote.toRequest
import com.ddd.attendance.domain.model.vote.ActiveVote
import com.ddd.attendance.domain.model.vote.FeedbackResults
import com.ddd.attendance.domain.model.vote.FeedbackTemplate
import com.ddd.attendance.domain.model.vote.MyVoteStatus
import com.ddd.attendance.domain.model.vote.TeamVoteResults
import com.ddd.attendance.domain.model.vote.TeamVoteTemplate
import com.ddd.attendance.domain.model.vote.VoteDetail
import com.ddd.attendance.domain.model.vote.VoteNonResponders
import com.ddd.attendance.domain.model.vote.VoteParticipation
import com.ddd.attendance.domain.model.vote.VoteSubmission
import com.ddd.attendance.domain.model.vote.VoteSummary
import com.ddd.attendance.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val apiVoteDataSource: ApiVoteDataSource
) : VoteRepository {

    override fun getActiveVote(): Flow<ActiveVote?> = flow {
        apiVoteDataSource.getActiveVote()
            .onFailure { throw it }
            .onSuccess { emit(it?.toDomain()) }
    }

    override fun getTeamVoteTemplate(voteId: Int): Flow<TeamVoteTemplate> = flow {
        apiVoteDataSource.getTeamVoteTemplate(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }

    override fun getFeedbackTemplate(voteId: Int): Flow<FeedbackTemplate> = flow {
        apiVoteDataSource.getFeedbackTemplate(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }

    override fun getMyVoteStatus(voteId: Int): Flow<MyVoteStatus> = flow {
        apiVoteDataSource.getMyVoteStatus(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }

    override fun submitVote(voteId: Int, submission: VoteSubmission): Flow<Unit> = flow {
        apiVoteDataSource.submitVote(voteId, submission.toRequest())
            .onFailure { throw it }
            .onSuccess { emit(Unit) }
    }

    override fun getVotes(): Flow<List<VoteSummary>> = flow {
        apiVoteDataSource.getVotes()
            .onFailure { throw it }
            .onSuccess { list -> emit(list.map { it.toDomain() }) }
    }

    override fun openVote(voteId: Int): Flow<Unit> = flow {
        apiVoteDataSource.openVote(voteId)
            .onFailure { throw it }
            .onSuccess { emit(Unit) }
    }

    override fun closeVote(voteId: Int): Flow<Unit> = flow {
        apiVoteDataSource.closeVote(voteId)
            .onFailure { throw it }
            .onSuccess { emit(Unit) }
    }

    override fun getVoteParticipation(voteId: Int): Flow<VoteParticipation> = flow {
        apiVoteDataSource.getVoteParticipation(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }

    override fun getNonResponders(voteId: Int): Flow<VoteNonResponders> = flow {
        apiVoteDataSource.getNonResponders(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }

    override fun getVoteDetail(voteId: Int): Flow<VoteDetail> = flow {
        apiVoteDataSource.getVoteDetail(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }

    override fun getTeamVoteResults(voteId: Int): Flow<TeamVoteResults> = flow {
        apiVoteDataSource.getTeamVoteResults(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }

    override fun getFeedbackResults(voteId: Int): Flow<FeedbackResults> = flow {
        apiVoteDataSource.getFeedbackResults(voteId)
            .onFailure { throw it }
            .onSuccess { emit(it.toDomain()) }
    }
}
