package com.ddd.attendance.data.api.datasource

import com.ddd.attendance.data.api.AdminMyPageApi
import com.ddd.attendance.data.datasource.ApiAdminMyPageDataSource
import com.ddd.attendance.data.model.admin.AdminAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminMeResponse
import com.ddd.attendance.data.model.admin.AdminScheduleTeamAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminTeamResponse
import javax.inject.Inject

class ApiAdminMyPageDataSourceImpl @Inject constructor (
    private val apiAdminMyPageApi: AdminMyPageApi
): ApiAdminMyPageDataSource {
    override suspend fun adminMe(): Result<AdminMeResponse> {
        return try {
            Result.success(apiAdminMyPageApi.adminMe())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun adminScheduleTeamAttendances(
        scheduleId: Int,
        teamId: Int
    ): Result<List<AdminScheduleTeamAttendanceResponse>> {
        val data =
            apiAdminMyPageApi.adminScheduleTeamAttendances(
                scheduleId = scheduleId,
                teamId = teamId
            )
        return try {
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun adminScheduleAttendances(scheduleId: Int): Result<AdminAttendanceResponse> {
        val data =
            apiAdminMyPageApi.adminScheduleAttendances(
                scheduleId = scheduleId
            )
        return try {
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun adminTeams(): Result<List<AdminTeamResponse>> {
        return try {
            Result.success(apiAdminMyPageApi.adminTeams())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}