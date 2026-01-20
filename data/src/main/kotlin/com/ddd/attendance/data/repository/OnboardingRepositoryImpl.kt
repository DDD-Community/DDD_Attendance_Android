package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiOnboardingDataSource
import com.ddd.attendance.data.mapper.onboarding.toDomain
import com.ddd.attendance.data.mapper.onboarding.toItemSelectJobDomain
import com.ddd.attendance.data.mapper.onboarding.toItemSelectRoleDomain
import com.ddd.attendance.data.mapper.onboarding.toItemSelectTeamDomain
import com.ddd.attendance.domain.model.onboarding.ItemSelect
import com.ddd.attendance.domain.model.onboarding.VerifyCode
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

    override fun getAdminSelectList(id: Int): Flow<Map<String, List<ItemSelect>>> = flow {
        val (jobResult, roleResult, teamResult) = coroutineScope {
            val jobDeferred = async { apiLoginDataSource.getJobs() }
            val roleDeferred = async { apiLoginDataSource.getRole() }
            val teamDeferred = async { apiLoginDataSource.getTeams(id) }

            Triple(jobDeferred.await(), roleDeferred.await(), teamDeferred.await())
        }

        val jobs = jobResult.getOrElse { emptyList() }
        val roles = roleResult.getOrElse { emptyList() }
        val teams = teamResult.getOrElse { emptyList() }

        emit(
            linkedMapOf(
                "job" to jobs.toItemSelectJobDomain(),
                "role" to roles.toItemSelectRoleDomain(),
                "team" to teams.toItemSelectTeamDomain()
            )
        )
    }
}