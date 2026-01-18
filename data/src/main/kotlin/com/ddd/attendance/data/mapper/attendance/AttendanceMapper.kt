package com.ddd.attendance.data.mapper.attendance

import com.ddd.attendance.data.model.AttendanceStatusResponse
import com.ddd.attendance.domain.model.attendance.AttendanceStatus

fun List<AttendanceStatusResponse>.toAttendanceStatusDomain(): List<AttendanceStatus> {
    return map { response ->
        AttendanceStatus(
            name = response.name,
            code = response.code
        )
    }
}