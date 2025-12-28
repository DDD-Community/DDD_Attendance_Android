package com.ddd.attendance.feature.admin

import com.ddd.attendance.feature.core.model.AttendanceStatus
import com.ddd.attendance.feature.core.model.AttendanceUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AdminUiState(
    val uiType: AdminType = AdminType.Attendance,
    val nextScheduleDate: String = "2026.03.07",
    val attendanceStatusList: ImmutableList<AttendanceUiModel> =
        persistentListOf(
            AttendanceUiModel(AttendanceStatus.ATTENDANCE, 3),
            AttendanceUiModel(AttendanceStatus.LATE, 1),
            AttendanceUiModel(AttendanceStatus.ABSENT, 2)
        ),
    val selectedTeamIndex: Int = 0,
)