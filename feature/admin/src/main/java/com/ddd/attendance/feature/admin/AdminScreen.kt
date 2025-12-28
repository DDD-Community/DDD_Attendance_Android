package com.ddd.attendance.feature.admin

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.admin.attendance.AttendanceScreen
import com.ddd.attendance.feature.admin.schedule.ScheduleScreen
import com.ddd.attendance.feature.core.header.UserHeader
import com.ddd.attendance.feature.core.model.AttendanceUiModel
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
        attendanceList = uiState.attendanceStatusList,
        selectedTeamIndex = uiState.selectedTeamIndex,
        onTabClick = {
            viewModel.onIntent(AdminIntent.TabChanged(it))
        }
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    uiType: AdminType,
    nextScheduleDate: String,
    attendanceList: ImmutableList<AttendanceUiModel>,
    selectedTeamIndex: Int,
    onTabClick:(Int) -> Unit
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
                        attendanceList = attendanceList,
                        selectedTeamIndex = selectedTeamIndex,
                        onTabClick = { onTabClick(it) }
                    )
                }
                AdminType.Schedule -> {
                    ScheduleScreen()
                }
            }
        }
    }
}