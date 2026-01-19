package com.ddd.attendance.feature.admin.attendance.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.ddd.attendance.feature.admin.attendance.R
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.StatusCautionary
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.Transparent

enum class MemberAttendanceType(
    val type: String,
    val displayName: String,
    @DrawableRes val iconRes: Int? = null,
    val textColor: Color = Color.Unspecified
) {
    NONE("NONE", "", iconRes = null, textColor = Transparent),
    ATTENDANCE("ATTENDED", "출석", iconRes = R.drawable.ic_attendance, textColor = TextPrimary),
    LATE("LATE", "지각", iconRes = R.drawable.ic_late, textColor = StatusCautionary),
    ABSENT("ABSENT", "결석", iconRes = R.drawable.ic_absent, textColor = BorderDisabled);

    companion object {
        fun fromType(type: String): MemberAttendanceType =
            entries.firstOrNull { it.type == type } ?: NONE

        fun displayNameOf(type: String): String =
            fromType(type).displayName
    }
}