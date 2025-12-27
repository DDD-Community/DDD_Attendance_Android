package com.ddd.attendance.feature.admin.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.core.board.AttendanceStatusBoard
import com.ddd.attendance.feature.core.model.AttendanceUiModel
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun AttendanceScreen(
    modifier: Modifier = Modifier,
    nextScheduleDate: String,
    attendanceList: ImmutableList<AttendanceUiModel>
) {
    Content(
        nextScheduleDate = nextScheduleDate,
        attendanceList = attendanceList
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    nextScheduleDate: String,
    attendanceList: ImmutableList<AttendanceUiModel>
) {
    Column(
       modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .height(46.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DddText(
                text = "\uD83D\uDDD3\uFE0F",
                style = Typography.bodyLargeM,
            )

            Spacer(modifier = Modifier.width(4.dp))

            DddText(
                text = nextScheduleDate,
                style = Typography.bodyLargeM,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        AttendanceStatusBoard(
            modifier = Modifier.padding(horizontal = 24.dp),
            items = attendanceList,
            onInfoClick = {

            }
        )
    }
}