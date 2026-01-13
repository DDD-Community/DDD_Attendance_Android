package com.ddd.attendance.data.mapper.schedule

import com.ddd.attendance.data.model.ScheduleResponse
import com.ddd.attendance.domain.model.Schedule

fun ScheduleResponse.toDomain(): Schedule {
    return Schedule(
        id = id,
        name = name,
        status = status,
        desc = desc,
        month = month,
        day = day
    )
}
