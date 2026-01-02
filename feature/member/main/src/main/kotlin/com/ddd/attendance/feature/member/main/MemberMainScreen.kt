package com.ddd.attendance.feature.member.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            MemberMainHeader(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToAttendance = onNavigateToAttendance
            )

            MemberMainAttendanceSection(
                memberName = uiState.memberName,
                activityPeriod = uiState.activityPeriod,
                attendanceStats = uiState.attendanceStats
            )

            MemberMainScheduleSection(
                scheduleItems = uiState.scheduleItems
            )
        }
    }
}

@Composable
fun MemberMainAttendanceSection(
    memberName: String,
    activityPeriod: String,
    attendanceStats: AttendanceStats
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
            )
        }
    }
}

@Composable
fun MemberMainScheduleSection(
    scheduleItems: List<ScheduleItem>
) {
    Column(
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Text(
            text = "12기 일정표",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(scheduleItems.size) { index ->
                val item = scheduleItems[index]
                ScheduleItemComposable(
                    date = item.date,
                    title = item.title,
                    subtitle = item.subtitle
                )
                if (index < scheduleItems.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun AttendanceCard(
    modifier: Modifier = Modifier,
    count: String,
    label: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFFEAEAEA)
        )
    }
}

@Composable
fun ScheduleItemComposable(
    date: String,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF2C2C2E),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(0xFF3A3A3C),
                    RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF8E8E93)
            )
        }
    }
}


@Composable
private fun MemberMainHeader(
    onNavigateToProfile: () -> Unit,
    onNavigateToAttendance: () -> Unit,
) {
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
}