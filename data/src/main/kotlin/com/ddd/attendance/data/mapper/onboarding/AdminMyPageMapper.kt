package com.ddd.attendance.data.mapper.onboarding

import com.ddd.attendance.data.model.admin.AdminAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminMeResponse
import com.ddd.attendance.data.model.admin.AdminScheduleTeamAttendanceResponse
import com.ddd.attendance.data.model.admin.AdminTeamResponse
import com.ddd.attendance.domain.model.admin.AdminAttendance
import com.ddd.attendance.domain.model.admin.AdminMe
import com.ddd.attendance.domain.model.admin.AdminScheduleTeamAttendance
import com.ddd.attendance.domain.model.admin.AdminTeam

fun AdminMeResponse.toDomain(): AdminMe {
    return AdminMe(
        userId = userId,
        name = name,
        email = email,
        generation = generation,
        team = team,
        jobRole = jobRole,
        role = role,
        managerRoles = managerRoles
    )
}

fun List<AdminScheduleTeamAttendanceResponse>.toAdminScheduleTeamAttendanceDomain(): List<AdminScheduleTeamAttendance> {
    return map { response ->
        AdminScheduleTeamAttendance(
            attendanceId = response.attendanceId?: 0,
            userId = response.userId?: 0,
            userName = response.userName?: "",
            userInfo = response.userInfo?: "",
            attendanceStatus = response.attendanceStatus?: "NONE"
        )
    }
}

fun AdminAttendanceResponse.toDomain(): AdminAttendance {
    return AdminAttendance(
        attended = totalAttended,
        late = totalLate,
        absent = totalAbsent
    )
}

fun List<AdminTeamResponse>.toAdminTeamDomain(): List<AdminTeam> {
    return map { response ->
        AdminTeam(
            teamId = response.teamId,
            name = response.name
        )
    }
}