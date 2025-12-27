package com.ddd.attendance.feature.core.model

import androidx.compose.ui.graphics.Color
import com.ddd.attendance.feature.core.R
import com.ddd.attendance.feature.designsystem.theme.FailError
import com.ddd.attendance.feature.designsystem.theme.StatusCautionary
import com.ddd.attendance.feature.designsystem.theme.TextPrimary

enum class AttendanceType(
    val labelRes: Int,
    val activeColor: Color
) {
    ATTENDANCE(
        labelRes = R.string.attendance,
        activeColor = TextPrimary
    ),
    LATE(
        labelRes = R.string.late,
        activeColor = StatusCautionary
    ),
    ABSENT(
        labelRes = R.string.absent,
        activeColor = FailError
    )
}