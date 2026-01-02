package com.ddd.attendance.feature.admin.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.admin.attendance.AttendanceScreen
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceInfo
import com.ddd.attendance.feature.admin.schedule.ScheduleScreen
import com.ddd.attendance.feature.core.header.UserHeader
import com.ddd.attendance.feature.core.model.UserType
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AdminScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiType = uiState.uiType,
        nextScheduleDate = uiState.nextScheduleDate,
        attendance = uiState.dummyAttendanceStatus.attendance,
        late = uiState.dummyAttendanceStatus.late,
        absent = uiState.dummyAttendanceStatus.absent,
        memberAttendanceInfos = uiState.dummyMemberAttendanceInfos,
        teamList = uiState.dummyTeamList,
        selectedTeamIndex = uiState.selectedTeamIndex,
        onTabClick = {
            viewModel.onIntent(AdminIntent.TabChanged(it))
        },
        onEditClick = {
            viewModel.onIntent(AdminIntent.ShowEditPopup)
        }
    )

    if (uiState.isShowEditPopup) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(AdminIntent.DismissEditPopup) },
            title = { Text("편집") },
            text = { Text("편집 화면으로 이동하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onIntent(AdminIntent.DismissEditPopup) }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIntent(AdminIntent.DismissEditPopup) }
                ) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    uiType: AdminType,
    nextScheduleDate: String,
    attendance: Int,
    late: Int,
    absent: Int,
    memberAttendanceInfos: ImmutableList<MemberAttendanceInfo>,
    teamList: ImmutableList<String>,
    selectedTeamIndex: Int,
    onTabClick:(Int) -> Unit,
    onEditClick:() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        UserHeader(
            modifier = modifier,
            type = UserType.Admin,
        ) {
            Log.d("UserHeader", "클릭")
            //show select box
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            when (uiType) {
                AdminType.Attendance -> {
                    AttendanceScreen(
                        nextScheduleDate = nextScheduleDate,
                        attendance = attendance,
                        late = late,
                        absent = absent,
                        memberAttendanceInfos = memberAttendanceInfos,
                        teamList = teamList,
                        selectedTeamIndex = selectedTeamIndex,
                        onTabClick = { onTabClick(it) },
                        onEditClick = onEditClick
                    )
                }
                AdminType.Schedule -> {
                    ScheduleScreen()
                }
            }
        }
    }
}