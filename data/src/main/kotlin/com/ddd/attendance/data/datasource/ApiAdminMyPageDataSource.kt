package com.ddd.attendance.data.datasource

import com.ddd.attendance.data.model.admin.AdminAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminMeResponse
import com.ddd.attendance.data.model.admin.AdminScheduleTeamAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminTeamResponse

interface ApiAdminMyPageDataSource {
    suspend fun adminMe(): Result<AdminMeResponse>
    suspend fun adminScheduleTeamAttendances(scheduleId: Int, teamId: Int): Result<List<AdminScheduleTeamAttendanceResponse>>
    suspend fun adminScheduleAttendances(scheduleId: Int): Result<AdminAttendanceResponse>
    suspend fun adminTeams(): Result<List<AdminTeamResponse>>
}