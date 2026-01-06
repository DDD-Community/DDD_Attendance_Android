package com.ddd.attendance.feature.admin.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.admin.schedule.model.Schedule
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundDefault
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondary
import com.ddd.attendance.feature.designsystem.theme.NeutralBlue20
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    scheduleList: ImmutableList<Schedule>
) {
    Content(
        scheduleList = scheduleList
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    scheduleList: ImmutableList<Schedule>
) {
    Box() {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            DddText(
                text = stringResource(R.string.schedule_13th),
                style = Typography.titleMediumM
            )

            Spacer(modifier = Modifier.height(16.dp))

            ScheduleList(
                items = scheduleList
            )
        }
    }
}

@Composable
private fun ScheduleList(
    items: ImmutableList<Schedule>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(items) { index, item ->
            ScheduleItem(
                month = item.month,
                day = item.day,
                title = item.title,
                description = item.description
            )
        }
    }
}

@Composable
private fun ScheduleItem(
    modifier: Modifier = Modifier,
    month: String,
    day: String,
    title: String,
    description: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
            .background(
                color = BackgroundSecondary,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .size(54.dp)
                .background(
                    color = NeutralBlue20,
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DddText(
                text = month,
                style = Typography.bodySmallM,
                color = BackgroundDefault
            )

            DddText(
                text = day,
                style = Typography.titleSmallM,
                color = BackgroundDefault
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            DddText(
                text = title,
                style = Typography.bodyLargeB,
            )

            DddText(
                text = description,
                style = Typography.bodySmallR
            )
        }
    }
}