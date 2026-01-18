package com.ddd.attendance.feature.admin.main

import com.ddd.attendance.domain.model.admin.AdminScheduleTeamAttendance
import com.ddd.attendance.domain.model.admin.AdminTeam
import com.ddd.attendance.domain.model.attendance.AttendanceStatus
import com.ddd.attendance.feature.admin.attendance.model.AttendanceBoardStatus
import com.ddd.attendance.feature.admin.schedule.model.ScheduleUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AdminUiState(
    val uiType: AdminType = AdminType.Attendance,
    val nextScheduleDate: String = "",
    val selectedTeamId: Int = 0,
    val selectedTeamIndex: Int = 0,
    val isShowEditPopup: Boolean = false,
    val isShowScreenChangeDropDown: Boolean = false,
    val isShowScheduleBottomSheet: Boolean = false,
    val isShowAbsentNotificationPopup: Boolean = false,
    val isShowQrScanner: Boolean = false,
    val isAttendanceSuccess: Boolean = false,

    val selectedEditText: String = "",

    val selectedScheduleId: Long = 0,
    val selectedStatus: String = "",
    val selectedAttendanceId: Int = 0,
    val selectedUserId: Int = 0,

    val teams: ImmutableList<AdminTeam> = persistentListOf(),
    val schedules: ImmutableList<ScheduleUiModel> = persistentListOf(),
    val memberAttendances: ImmutableList<AdminScheduleTeamAttendance> = persistentListOf(),
    val attendanceBoardStatus: AttendanceBoardStatus = AttendanceBoardStatus(attendance = 0, late = 0, absent = 0),
    val attendanceStatusList: ImmutableList<AttendanceStatus> = persistentListOf()
)