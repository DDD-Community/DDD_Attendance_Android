package com.ddd.attendance.feature.admin.attendance

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.domain.model.admin.AdminScheduleTeamAttendance
import com.ddd.attendance.domain.model.admin.AdminTeam
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceType
import com.ddd.attendance.feature.core.board.AttendanceStatusBoard
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundDefault
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.ButtonDisabled
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AttendanceScreen(
    modifier: Modifier = Modifier,
    nextScheduleDate: String,
    attendance: Int,
    late: Int,
    absent: Int,
    memberAttendances: ImmutableList<AdminScheduleTeamAttendance>,
    teamList: ImmutableList<AdminTeam>,
    selectedTeamIndex: Int = 0,
    onTabClick:(teamId: Int, selectedIndex: Int) -> Unit,
    onEditClick:(selectedEditText: String, attendanceId: Int, userId: Int) -> Unit,
    onDataClick: () -> Unit,
    onAbsentNotificationClick: () -> Unit
) {
    Content(
        nextScheduleDate = nextScheduleDate,
        attendance = attendance,
        late = late,
        absent = absent,
        memberAttendances = memberAttendances,
        teamList = teamList,
        selectedTeamIndex = selectedTeamIndex,
        onTabClick = { teamId, selectedIndex ->
            onTabClick(teamId, selectedIndex)
        },
        onEditClick = onEditClick,
        onDataClick = onDataClick,
        onAbsentNotificationClick = onAbsentNotificationClick
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    nextScheduleDate: String,
    attendance: Int,
    late: Int,
    absent: Int,
    memberAttendances: ImmutableList<AdminScheduleTeamAttendance>,
    teamList: ImmutableList<AdminTeam>,
    selectedTeamIndex: Int,
    onTabClick:(teamId: Int, selectedIndex: Int) -> Unit,
    onEditClick:(selectedEditText: String, attendanceId: Int, userId: Int) -> Unit,
    onDataClick: () -> Unit,
    onAbsentNotificationClick: () -> Unit
) {
    Column(
       modifier = modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .height(46.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDataClick
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (nextScheduleDate.isNotEmpty()) {
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
        }

        Spacer(modifier = Modifier.height(6.dp))

        AttendanceStatusBoard(
            modifier = Modifier.padding(horizontal = 24.dp),
            attendance = attendance,
            late = late,
            absent = absent,
            onAbsentNotificationClick = onAbsentNotificationClick
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (teamList.isNotEmpty()) {
            TeamTabBar(
                tabs = teamList,
                selectedIndex = selectedTeamIndex,
                onTabClick = { teamId, index ->
                    onTabClick(teamId, index)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TeamCardList(
                memberAttendances = memberAttendances,
                selectedTeamName = teamList[selectedTeamIndex].name,
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
fun TeamTabBar(
    tabs: ImmutableList<AdminTeam>,
    selectedIndex: Int,
    onTabClick:(teamId: Int, selectedIndex: Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(tabs) { index, item ->
            if (index == 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }

            TabItem(
                title = item.name,
                selected = index == selectedIndex,
                onClick = { onTabClick(tabs[index].teamId, index) }
            )

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun TabItem(
    title: String,
    selected: Boolean,
    onClick:() -> Unit
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
            color = if (selected) TextPrimary else TextSecondaryDark,
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
fun TeamCardList(
    memberAttendances: ImmutableList<AdminScheduleTeamAttendance>,
    selectedTeamName: String,
    onEditClick:(selectedEditText: String, attendanceId: Int, userId: Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(memberAttendances) { _, item ->

            CardItem(
                name = item.userName,
                team = selectedTeamName,
                role = item.userInfo,
                attendanceStatus = item.attendanceStatus
            ) {
                val name = MemberAttendanceType.displayNameOf(item.attendanceStatus)
                Log.d("MemberAttendanceType-displayNameOf", "변환 전 : ${ item.attendanceStatus }, 변환 후 : $name")

                onEditClick(
                    name,
                    item.attendanceId,
                    item.userId
                )
            }
        }
    }
}

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    name: String,
    team: String,
    role: String,
    attendanceStatus: String,
    onEditClick: () -> Unit
) {
    val memberType = MemberAttendanceType.fromType(attendanceStatus)

    val isDisable = memberType == MemberAttendanceType.ABSENT

    val borderModifier = if (isDisable) {
        Modifier.drawBehind {
            val strokeWidth = 2.dp.toPx()
            val cornerRadius = 16.dp.toPx()

            drawRoundRect(
                color = BorderDisabled,
                size = size,
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(4.dp.toPx(), 4.dp.toPx())
                    )
                ),
                cornerRadius = CornerRadius(cornerRadius)
            )
        }.background(color = BackgroundDefault, shape = RoundedCornerShape(16.dp))
    } else {
        Modifier.background(color = BackgroundSecondaryDark, shape = RoundedCornerShape(16.dp))
    }

    Box(
        modifier = modifier
            .height(84.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .then(borderModifier)
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
                    style = Typography.titleSmallB,
                    color = if (isDisable) BorderDisabled else TextPrimary
                )
                Row {
                    DddText(
                        text = team,
                        style = Typography.bodyMediumM,
                        color = if (isDisable) BorderDisabled else TextSecondaryDark
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    DddText(
                        text = "/ $role",
                        style = Typography.bodyMediumM,
                        color = if (isDisable) BorderDisabled else TextDisabled
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1F))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 텍스트
                val text = memberType.displayName

                if (text.isNotBlank()) {
                    DddText(
                        text = text,
                        style = Typography.bodyMediumM,
                        color = memberType.textColor
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // 상태 아이콘
                memberType.iconRes?.let {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(it),
                        contentDescription = "출석 상태 아이콘"
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // 수정 아이콘
                Image(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onEditClick()
                        },
                    painter = painterResource(id = R.drawable.ic_edit_pencil),
                    contentDescription = "수정 아이콘",
                )
            }
        }
    }
}