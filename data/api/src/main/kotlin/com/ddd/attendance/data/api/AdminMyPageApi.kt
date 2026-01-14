package com.ddd.attendance.data.api

import com.ddd.attendance.data.model.admin.AdminAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminMeResponse
import com.ddd.attendance.data.model.admin.AdminScheduleTeamAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminTeamResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AdminMyPageApi {

    @GET("api/admin/me")
    suspend fun adminMe(): AdminMeResponse

    @GET("api/admin/me/schedules/{scheduleId}/teams/{teamId}/attendances")
    suspend fun adminScheduleTeamAttendances(
        @Path("scheduleId") scheduleId: Int,
        @Path("teamId") teamId: Int
    ): List<AdminScheduleTeamAttendanceResponse>

    @GET("api/admin/me/schedules/{scheduleId}/attendances")
    suspend fun adminScheduleAttendances(
        @Path("scheduleId") scheduleId: Int
    ): AdminAttendanceResponse

    @GET("api/admin/me/generations/teams")
    suspend fun adminTeams(): List<AdminTeamResponse>
}