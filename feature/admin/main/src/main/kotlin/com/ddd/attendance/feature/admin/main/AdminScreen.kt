package com.ddd.attendance.feature.admin.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.feature.admin.attendance.AttendanceScreen
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceInfo
import com.ddd.attendance.feature.admin.main.dropdown.EditPopupDropdown
import com.ddd.attendance.feature.admin.main.dropdown.ScreenChangeDropDown
import com.ddd.attendance.feature.admin.schedule.ScheduleScreen
import com.ddd.attendance.feature.core.header.UserHeader
import com.ddd.attendance.feature.core.model.UserType
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondary
import com.ddd.attendance.feature.designsystem.theme.BorderAlternative
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.designsystem.theme.White
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
        editItems = uiState.dummySelectedEditPopupItemList,
        teamList = uiState.dummyTeamList,
        selectedTeamIndex = uiState.selectedTeamIndex,
        isEditDialogVisible = uiState.isShowEditPopup,
        isShowScreenChangeDropDownVisible = uiState.isShowScreenChangeDropDown,
        selectedEditText = uiState.selectedEditText,
        onTabClick = {
            viewModel.onIntent(AdminIntent.TabChanged(it))
        },
        onEditClick = {
            viewModel.onIntent(AdminIntent.ShowEditPopup)
        },
        onEditConfirm = {
            viewModel.onIntent(AdminIntent.HideEditPopup)
        },
        onEditItemSelected = {
            viewModel.onIntent(AdminIntent.DropDownTextChanged(it))
        },
        onHeaderClick = {
            viewModel.onIntent(AdminIntent.ShowDropDownScreenChange)
        },
        onScreenChangeDropDownDismiss = {
            viewModel.onIntent(AdminIntent.HideDropDownScreenChange)

        },
        onUiTypeChanged = {
            viewModel.onIntent(AdminIntent.ScreenUiTypeChanged(it))
        },

    )
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
    editItems: ImmutableList<String>,
    teamList: ImmutableList<String>,
    selectedTeamIndex: Int,
    isEditDialogVisible: Boolean,
    isShowScreenChangeDropDownVisible: Boolean,
    selectedEditText: String,
    onTabClick: (Int) -> Unit,
    onEditClick: () -> Unit,
    onEditConfirm: () -> Unit,
    onEditItemSelected: (String) -> Unit,
    onHeaderClick:() -> Unit,
    onUiTypeChanged: (AdminType) -> Unit,
    onScreenChangeDropDownDismiss: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            UserHeader(
                modifier = modifier,
                type = UserType.Admin,
            ) {
                onHeaderClick()
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

        if (isEditDialogVisible) {
            EditPopup(
                title = stringResource(R.string.attendance_change_confirm),
                items = editItems,
                selectedText = selectedEditText,
                onConfirm = onEditConfirm,
                onItemSelected = {
                    onEditItemSelected(it)
                }
            )
        }

        if (isShowScreenChangeDropDownVisible) {
            ScreenChangeDropDown(
                onScreenChangeDropDownDismiss = onScreenChangeDropDownDismiss,
                onUiTypeChanged = { onUiTypeChanged(it) }
            )
        }
    }
}

@Composable
internal fun EditPopup(
    modifier: Modifier = Modifier,
    title: String,
    selectedText: String,
    items: ImmutableList<String>,
    onConfirm: () -> Unit,
    onItemSelected: (String) -> Unit
) {
    var isEditPopupListExpanded by remember { mutableStateOf(false) }
    var anchorWidth by remember { mutableStateOf(0.dp) }
    var anchorHeight by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp)
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 36.dp
            )
        ) {
            DddText(
                text = title,
                style = Typography.titleSmallB,
                color = BackgroundSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .background(
                            color = BorderAlternative,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            anchorWidth = with(density) { coordinates.size.width.toDp() }
                            anchorHeight = coordinates.size.height
                        }
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            isEditPopupListExpanded = true
                        }
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DddText(
                        text = selectedText,
                        style = Typography.bodyLargeM,
                        color = BackgroundSecondary
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(
                            com.ddd.attendance.feature.core.R.drawable.ic_bottom_arrow_dark
                        ),
                        contentDescription = null
                    )
                }

                EditPopupDropdown(
                    anchorWidth = anchorWidth,
                    anchorHeightPx = anchorHeight,
                    expanded = isEditPopupListExpanded,
                    items = items,
                    onItemSelected = {
                        onItemSelected(it)
                        isEditPopupListExpanded = false
                    },
                    onDismiss = {
                        isEditPopupListExpanded = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonEnabled
                ),
                onClick = onConfirm
            ) {
                DddText(
                    text = stringResource(R.string.confirm),
                    style = Typography.bodySmallM
                )
            }
        }
    }
}