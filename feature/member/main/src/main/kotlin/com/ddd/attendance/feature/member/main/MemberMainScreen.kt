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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
            modifier = Modifier.fillMaxSize()
        ) {
            MemberMainHeader(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToAttendance = onNavigateToAttendance
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
                    attendanceStats = uiState.attendanceStats
                )

                MemberMainScheduleSection(
                    generationNumber = uiState.generationNumber,
                    scheduleItems = uiState.scheduleItems
                )
            }
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

        scheduleItems.forEachIndexed { _, item ->
            ScheduleItemComposable(
                date = item.date,
                title = item.title,
                subtitle = item.subtitle
            )

            Spacer(modifier = Modifier.height(12.dp))
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
        ScheduleDateBox(
            date = date,
            modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = subtitle,
                fontSize = 15.sp,
                color = Color(0xFFEAEAEA)
            )
        }
    }
}

@Composable
fun ScheduleDateBox(
    date: String,
    modifier: Modifier = Modifier
) {
    val dateParts = date.split("\n")
    val month = dateParts.getOrNull(0) ?: ""
    val day = dateParts.getOrNull(1) ?: ""
    
    Box(
        modifier = modifier
            .background(
                Color(0xFFE1EAFF),
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
                color = Color(0xFF0C0E0F)
            )
            
            Text(
                text = day,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C0E0F)
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