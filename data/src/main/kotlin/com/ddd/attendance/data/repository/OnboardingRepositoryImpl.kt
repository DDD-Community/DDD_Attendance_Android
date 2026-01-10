package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiOnboardingDataSource
import com.ddd.attendance.data.mapper.toDomain
import com.ddd.attendance.data.mapper.toJobRoleDomain
import com.ddd.attendance.data.mapper.toTeamDomain
import com.ddd.attendance.domain.model.JobRole
import com.ddd.attendance.domain.model.Team
import com.ddd.attendance.domain.model.VerifyCode
import com.ddd.attendance.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val apiLoginDataSource: ApiOnboardingDataSource
) : OnboardingRepository {
    override fun verifyCode(code: String): Flow<VerifyCode> = flow {
        val result = apiLoginDataSource.verifyCode(code)
        result
            .onSuccess { response ->
                emit(response.toDomain())
            }
            .onFailure { throw it }
    }

    override fun getTeams(id: Int): Flow<List<Team>> = flow {
        val result = apiLoginDataSource.getTeams(id)
        result
            .onSuccess { response ->
                emit(response.toTeamDomain())
            }
            .onFailure { throw it }
    }

    override fun getJobs(): Flow<List<JobRole>> = flow {
        val result = apiLoginDataSource.getJobs()
        result
            .onSuccess { response ->
                emit(response.toJobRoleDomain())
            }
            .onFailure { throw it }
    }

    override fun getRole(): Flow<List<JobRole>> = flow {
        val result = apiLoginDataSource.getRole()
        result
            .onSuccess { response ->
                emit(response.toJobRoleDomain())
            }
            .onFailure { throw it }
    }
}