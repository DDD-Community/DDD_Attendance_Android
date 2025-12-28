package com.ddd.attendance.feature.admin.attendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.admin.MemberAttendanceType
import com.ddd.attendance.feature.core.R
import com.ddd.attendance.feature.core.board.AttendanceStatusBoard
import com.ddd.attendance.feature.core.model.AttendanceUiModel
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondary
import com.ddd.attendance.feature.designsystem.theme.ButtonDisabled
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondary
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun AttendanceScreen(
    modifier: Modifier = Modifier,
    nextScheduleDate: String,
    attendanceList: ImmutableList<AttendanceUiModel>,
    selectedTeamIndex: Int = 0,
    onTabClick: (Int) -> Unit,
) {
    Content(
        nextScheduleDate = nextScheduleDate,
        attendanceList = attendanceList,
        selectedTeamIndex = selectedTeamIndex,
        onTabClick = { onTabClick(it) }
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    nextScheduleDate: String,
    attendanceList: ImmutableList<AttendanceUiModel>,
    selectedTeamIndex: Int,
    onTabClick: (Int) -> Unit
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

        Spacer(modifier = Modifier.height(28.dp))

        TeamTabBar(
            tabs = listOf("web 1팀", "web 2팀", "Android 1팀", "Android 2팀", "iOS 1팀", "iOS 2팀"),
            selectedIndex = selectedTeamIndex,
            onTabClick = { onTabClick(it) }
        )

        TeamCardList()
    }
}

@Composable
fun TeamTabBar(
    tabs: List<String>,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(tabs) { index, title ->
            if (index == 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }

            TabItem(
                title = title,
                selected = index == selectedIndex,
                onClick = { onTabClick(index) }
            )

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun TabItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    var textWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = Typography.bodySmallM,
            color = if (selected) TextPrimary else TextSecondary,
            maxLines = 1,
            softWrap = false,
            onTextLayout = { result ->
                textWidth = result.size.width
            }
        )

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(
                    with(density) {
                        textWidth.toDp()
                    }
                )
                .height(1.dp)
                .background(if (selected) ButtonEnabled else ButtonDisabled)
        )
    }
}

@Composable
fun TeamCardList() {
    LazyColumn() {

    }
}

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    name: String,
    team: String,
    role: String,
    memberAttendanceType: MemberAttendanceType
) {
    Box(
        modifier = modifier
            .height(84.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(color = BackgroundSecondary, shape = RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
            ) {
                DddText(
                    text = name,
                    style = Typography.titleSmallB
                )
                Row {
                    DddText(
                        text = team,
                        style = Typography.bodyMediumM,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    DddText(
                        text = "/ $role",
                        style = Typography.titleSmallB,
                        color = TextDisabled
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1F))

            Row() {
                when(memberAttendanceType) {
                    MemberAttendanceType.NONE -> {

                    }
                    else -> {
                        //text, image, image
                        DddText(
                            text = "/ $role",
                            style = Typography.titleSmallB,
                            color = TextDisabled
                        )

                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.logo_ddd),
                            contentDescription = "출석 이미지",
                        )

                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.logo_ddd),
                            contentDescription = "수정 아이콘",
                        )
                    }
                }
            }
        }
    }
}