package com.ddd.attendance.feature.admin

data class AdminUiState(
    val uiType: AdminType = AdminType.Attendance,
    val nextScheduleDate: String = "2026.03.07"
) {

}