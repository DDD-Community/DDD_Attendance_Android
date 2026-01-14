package com.ddd.attendance.domain.model.admin

data class AdminAttendance(
    val attended: Int,
    val late: Int,
    val absent: Int
) {
    val totalCount: Int
        get() = attended + late + absent
}