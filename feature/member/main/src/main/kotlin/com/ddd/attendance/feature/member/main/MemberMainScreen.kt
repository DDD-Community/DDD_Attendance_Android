package com.ddd.attendance.feature.member.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ddd.attendance.feature.core.popup.OneButtonTitleContentPopup
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MemberMainScreen(
    modifier: Modifier = Modifier,
    onNavigateToProfile: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    viewModel: MemberMainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAbsentAlertPopup by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val hasUnattendedSchedule = uiState.scheduleItems.any { it.status.isEmpty() }
            MemberMainHeader(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToAttendance = onNavigateToAttendance,
                showQrTooltip = hasUnattendedSchedule
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                MemberMainAttendanceSection(
                    memberName = uiState.memberName,
                    activityPeriod = uiState.activityPeriod,
                    attendanceStats = uiState.attendanceStats,
                    onAlertClick = { showAbsentAlertPopup = true }
                )

                MemberMainScheduleSection(
                    generationNumber = uiState.generationNumber,
                    scheduleItems = uiState.scheduleItems
                )
            }
        }

        OneButtonTitleContentPopup(
            isShow = showAbsentAlertPopup,
            titleText = "주의해주세요!",
            contentText = "2번 지각 시 노쇼비를 돌려받을 수 없습니다.",
            onDismiss = { showAbsentAlertPopup = false }
        )
    }
}

@Composable
fun MemberMainAttendanceSection(
    memberName: String,
    activityPeriod: String,
    attendanceStats: AttendanceStats,
    onAlertClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        Text(
            text = "${memberName}님의 출석 현황",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "활동기간 : $activityPeriod",
            fontSize = 14.sp,
            color = Color(0xFFEAEAEA)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF202325),
                    RoundedCornerShape(12.dp)
                )
                .padding(24.dp)
        ) {
            AttendanceCard(
                count = attendanceStats.attendance.toString(),
                label = "출석",
                modifier = Modifier.weight(1f),
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(48.dp)
                    .align(Alignment.CenterVertically)
                    .background(Color(0xFF3A3A3C))
            )

            AttendanceCard(
                count = attendanceStats.late.toString(),
                label = "지각",
                modifier = Modifier.weight(1f),
                countColor = Color(0xFFFD5D08),
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(48.dp)
                    .align(Alignment.CenterVertically)
                    .background(Color(0xFF3A3A3C))
            )

            AttendanceCard(
                count = attendanceStats.absent.toString(),
                label = "결석",
                modifier = Modifier.weight(1f),
                countColor = Color(0xFFFD1008),
                showAlertIcon = true,
                onAlertClick = onAlertClick
            )
        }
    }
}

@Composable
fun MemberMainScheduleSection(
    generationNumber: Int,
    scheduleItems: List<ScheduleItem>
) {
    Column(
        modifier = Modifier.padding(top = 56.dp)
    ) {
        Text(
            text = "${generationNumber}기 일정표",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (scheduleItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_schedule_empty),
                    contentDescription = "일정 없음",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "아직 일정이 없어요.",
                    fontSize = 16.sp,
                    color = Color(0xFF6F6F6F)
                )
            }
        } else {
            scheduleItems.forEachIndexed { _, item ->
                ScheduleItemComposable(
                    date = item.date,
                    title = item.title,
                    subtitle = item.subtitle,
                    status = item.status
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AttendanceCard(
    modifier: Modifier = Modifier,
    count: String,
    label: String,
    countColor: Color = Color.White,
    labelColor: Color = Color(0xFFEAEAEA),
    showAlertIcon: Boolean = false,
    onAlertClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = countColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = labelColor
            )
            if (showAlertIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_alert),
                    contentDescription = "Alert",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onAlertClick() },
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun ScheduleItemComposable(
    date: String,
    title: String,
    subtitle: String,
    status: String = ""
) {
    val isAttendance = status.equals("ATTENDANCE", ignoreCase = true)
    val isLate = status.equals("LATE", ignoreCase = true)
    val isAbsent = status.equals("ABSENT", ignoreCase = true)

    val backgroundColor = when {
        isAttendance -> Color(0xFF0D82F9)
        isLate -> Color(0xFFFD5D08)
        isAbsent -> Color.Transparent
        else -> Color(0xFF202325)
    }

    val borderModifier = when {
        isAbsent -> Modifier.drawBehind {
            val strokeWidth = 1.dp.toPx()
            val dashLength = 4.dp.toPx()
            val gapLength = 4.dp.toPx()
            drawRoundRect(
                color = Color(0xFF6F6F6F),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashLength, gapLength),
                        0f
                    )
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
            )
        }
        else -> Modifier
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    backgroundColor,
                    RoundedCornerShape(12.dp)
                )
                .then(borderModifier)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScheduleDateBox(
                date = date,
                modifier = Modifier.size(54.dp),
                isAbsent = isAbsent
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAbsent) Color(0xFF6F6F6F) else Color.White
                )

                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    color = if (isAbsent) Color(0xFF6F6F6F) else Color(0xFFEAEAEA)
                )
            }
        }

        if (isAttendance || isLate) {
            Icon(
                painter = painterResource(
                    id = if (isAttendance) R.drawable.ic_stamp_attendance
                    else R.drawable.ic_stamp_late
                ),
                contentDescription = if (isAttendance) "출석" else "지각",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = 2.dp)
                    .size(width = 120.dp, height = 84.dp),
                tint = Color(0xFFFFFFFF)
            )
        }
    }
}

@Composable
fun ScheduleDateBox(
    date: String,
    modifier: Modifier = Modifier,
    isAbsent: Boolean = false
) {
    val dateParts = date.split("\n")
    val month = dateParts.getOrNull(0) ?: ""
    val day = dateParts.getOrNull(1) ?: ""

    val boxBackgroundColor = if (isAbsent) Color(0x33E1EAFF) else Color(0xCCE1EAFF)
    val textColor = Color(0xFF0C0E0F)

    Box(
        modifier = modifier
            .background(
                boxBackgroundColor,
                RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = month,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )

            Text(
                text = day,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
private fun MemberMainHeader(
    onNavigateToProfile: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    showQrTooltip: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(start = 16.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = com.ddd.attendance.feature.core.R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 10.dp, end = 9.dp, top = 8.dp, bottom = 8.dp),
                tint = Color.White
            )

            Row {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { onNavigateToAttendance() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = com.ddd.attendance.feature.core.R.drawable.ic_qr),
                            contentDescription = "QR",
                            modifier = Modifier.size(36.dp),
                            tint = Color.Unspecified
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clickable { onNavigateToProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = com.ddd.attendance.feature.core.R.drawable.ic_profile),
                        contentDescription = "Profile",
                        modifier = Modifier.size(36.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }

        if (showQrTooltip) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                // QR 아이콘 중앙 위치: 프로필(36dp) + Spacer(8dp) + QR절반(18dp) = 62dp
                // 말풍선 중앙: 148dp / 2 = 74dp
                // offset: 74 - 62 = 12dp (오른쪽으로 이동)
                Image(
                    painter = painterResource(id = R.drawable.ic_qr_tool_tip),
                    contentDescription = "QR 출석을 진행해주세요",
                    modifier = Modifier
                        .size(width = 148.dp, height = 42.dp)
                        .offset(x = 12.dp)
                )
            }
        }
    }
}