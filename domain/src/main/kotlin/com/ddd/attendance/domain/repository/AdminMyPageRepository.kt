package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.admin.AdminAttendance
import com.ddd.attendance.domain.model.admin.AdminMe
import com.ddd.attendance.domain.model.admin.AdminScheduleTeamAttendance
import com.ddd.attendance.domain.model.admin.AdminTeam
import kotlinx.coroutines.flow.Flow

interface AdminMyPageRepository {
    fun adminMe(): Flow<AdminMe>
    fun adminScheduleTeamAttendances(scheduleId: Int, teamId: Int): Flow<List<AdminScheduleTeamAttendance>>
    fun adminAttendances(scheduleId: Int): Flow<AdminAttendance>
    fun adminTeams(): Flow<List<AdminTeam>>
}