package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiOnboardingDataSource
import com.ddd.attendance.data.mapper.toDomain
import com.ddd.attendance.data.mapper.toItemSelectJobDomain
import com.ddd.attendance.data.mapper.toItemSelectRoleDomain
import com.ddd.attendance.data.mapper.toItemSelectTeamDomain
import com.ddd.attendance.domain.model.ItemSelect
import com.ddd.attendance.domain.model.VerifyCode
import com.ddd.attendance.domain.repository.OnboardingRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    override fun getMemberSelectList(id: Int): Flow<Map<String, List<ItemSelect>>> = flow {
        val (jobResult, teamResult) = coroutineScope {
            val jobDeferred = async { apiLoginDataSource.getJobs() }
            val teamDeferred = async { apiLoginDataSource.getTeams(id) }

            jobDeferred.await() to teamDeferred.await()
        }

        val jobs = jobResult.getOrElse { emptyList() }
        val teams = teamResult.getOrElse { emptyList() }

        emit(
            linkedMapOf(
                "job" to jobs.toItemSelectJobDomain(),
                "team" to teams.toItemSelectTeamDomain()
            )
        )
    }

    override fun getAdminSelectList(): Flow<Map<String, List<ItemSelect>>> = flow {
        val (jobResult, roleResult) = coroutineScope {
            val jobDeferred = async { apiLoginDataSource.getJobs() }
            val roleDeferred = async { apiLoginDataSource.getRole() }

            jobDeferred.await() to roleDeferred.await()
        }

        val roles = roleResult.getOrElse { emptyList() }
        val jobs = jobResult.getOrElse { emptyList() }

        emit(
            linkedMapOf(
                "job" to jobs.toItemSelectJobDomain(),
                "role" to roles.toItemSelectRoleDomain()
            )
        )
    }
}