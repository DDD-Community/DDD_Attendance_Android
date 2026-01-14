package com.ddd.attendance.feature.admin.schedule

import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.feature.admin.schedule.model.ScheduleUiModel

fun List<Schedule>.toUi(): List<ScheduleUiModel> =
    this.map { schedule ->
        ScheduleUiModel(
            id = schedule.id,
            month = schedule.month,
            day = schedule.month,
            name = schedule.name,
            desc = schedule.desc,
            isSelected = false
        )
    }