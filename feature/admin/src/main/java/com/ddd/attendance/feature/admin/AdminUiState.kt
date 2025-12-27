package com.ddd.attendance.feature.admin

import com.ddd.attendance.feature.core.model.AttendanceType
import com.ddd.attendance.feature.core.model.AttendanceUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AdminUiState(
    val uiType: AdminType = AdminType.Attendance,
    val nextScheduleDate: String = "2026.03.07",
    val attendanceList: ImmutableList<AttendanceUiModel> =
        persistentListOf(
            AttendanceUiModel(AttendanceType.ATTENDANCE, 3),
            AttendanceUiModel(AttendanceType.LATE, 1),
            AttendanceUiModel(AttendanceType.ABSENT, 2)
        )
)