package com.ddd.attendance.feature.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.admin.attendance.AttendanceScreen
import com.ddd.attendance.feature.admin.schedule.ScheduleScreen
import com.ddd.attendance.feature.designsystem.component.DDDText
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
fun AdminScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiType = uiState.uiType
    )
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    uiType: AdminType,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AdminHeader(
            modifier = modifier
        )
        AdminBody(
            modifier = modifier,
            uiType = uiType
        )

    }
}

@Composable
internal fun AdminHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            DDDText(
                text = stringResource(R.string.attendance),
                style = Typography.titleMediumB
            )

            Spacer(modifier = Modifier.width(2.dp))

            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.bottom_arrow_white),
                contentDescription = "UI 선택"
            )

            Spacer(modifier = Modifier.weight(1F))
        }
    }
}

@Composable
internal fun AdminBody(
    modifier: Modifier = Modifier,
    uiType: AdminType,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiType) {
            AdminType.Attendance -> AttendanceScreen()
            AdminType.Schedule -> ScheduleScreen()
        }
    }
}