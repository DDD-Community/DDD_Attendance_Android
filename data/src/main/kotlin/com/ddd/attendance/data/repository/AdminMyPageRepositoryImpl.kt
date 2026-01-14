package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiAdminMyPageDataSource
import com.ddd.attendance.data.mapper.onboarding.toAdminScheduleTeamAttendanceDomain
import com.ddd.attendance.data.mapper.onboarding.toAdminTeamDomain
import com.ddd.attendance.data.mapper.onboarding.toDomain
import com.ddd.attendance.domain.model.admin.AdminAttendance
import com.ddd.attendance.domain.model.admin.AdminMe
import com.ddd.attendance.domain.model.admin.AdminScheduleTeamAttendance
import com.ddd.attendance.domain.model.admin.AdminTeam
import com.ddd.attendance.domain.repository.AdminMyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AdminMyPageRepositoryImpl @Inject constructor(
    private val apiAdminMyPageDataSource: ApiAdminMyPageDataSource
): AdminMyPageRepository {

    override fun adminMe(): Flow<AdminMe> = flow {
        apiAdminMyPageDataSource.adminMe()
            .onSuccess { response ->
                emit(response.toDomain())
            }
            .onFailure { throw it }
    }

    override fun adminScheduleTeamAttendances(
        scheduleId: Int,
        teamId: Int
    ): Flow<List<AdminScheduleTeamAttendance>> = flow {
        apiAdminMyPageDataSource.adminScheduleTeamAttendances(
            scheduleId = scheduleId,
            teamId = teamId
        )
            .onSuccess { response ->
            emit(response.toAdminScheduleTeamAttendanceDomain())
        }
            .onFailure { throw it }
    }

    override fun adminAttendances(scheduleId: Int): Flow<AdminAttendance> = flow {
        apiAdminMyPageDataSource.adminScheduleAttendances(
            scheduleId = scheduleId
        ).onSuccess { response ->
            emit(response.toDomain())
        }
            .onFailure { throw it }
    }

    override fun adminTeams(): Flow<List<AdminTeam>> = flow {
        apiAdminMyPageDataSource.adminTeams()
            .onSuccess { response ->
                emit(response.toAdminTeamDomain())
            }
            .onFailure { throw it }
    }
}