package com.ddd.attendance.feature.admin.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.ddd.attendance.feature.admin.schedule.model.ScheduleUiModel
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundDefault
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.NeutralBlue20
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    scheduleList: ImmutableList<ScheduleUiModel>
) {
    Content(
        scheduleList = scheduleList
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    scheduleList: ImmutableList<ScheduleUiModel>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        DddText(
            text = stringResource(R.string.schedule_13th),
            style = Typography.titleMediumM
        )

        if (scheduleList.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(scheduleList) { index, item ->
                    ScheduleCard(
                        month = "${ item.month }월",
                        day = "${ item.day }",
                        title = item.name,
                        description = item.desc
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleCard(
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
                color = BackgroundSecondaryDark,
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
                color = TextPrimary
            )

            DddText(
                text = description,
                style = Typography.bodySmallR,
                color = TextSecondaryDark
            )
        }
    }
}