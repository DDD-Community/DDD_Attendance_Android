package com.ddd.attendance.feature.admin.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.domain.model.admin.AdminScheduleTeamAttendance
import com.ddd.attendance.domain.model.admin.AdminTeam
import com.ddd.attendance.domain.model.attendance.AttendanceStatus
import com.ddd.attendance.feature.admin.attendance.AttendanceScreen
import com.ddd.attendance.feature.admin.main.dropdown.EditPopupDropdown
import com.ddd.attendance.feature.admin.main.dropdown.ScreenChangeDropDown
import com.ddd.attendance.feature.admin.schedule.ScheduleScreen
import com.ddd.attendance.feature.admin.schedule.model.ScheduleUiModel
import com.ddd.attendance.feature.core.header.UserHeader
import com.ddd.attendance.feature.core.model.UserType
import com.ddd.attendance.feature.core.popup.OneButtonTitleContentPopup
import com.ddd.attendance.feature.core.qr.QrScanState
import com.ddd.attendance.feature.core.qr.composable.QrScannerScreen
import com.ddd.attendance.feature.designsystem.component.DddIconButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundDefault
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryLight
import com.ddd.attendance.feature.designsystem.theme.BorderAlternative
import com.ddd.attendance.feature.designsystem.theme.BorderEnabled
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.NeutralBlue20
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryLight
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

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                AdminNavigationEvent.PopBackStack -> { navController.popBackStack() }
                AdminNavigationEvent.GoToProfile -> { navController.navigate("ADMIN_PROFILE") }
                AdminNavigationEvent.GoToLogin -> {
                    navController.navigate("LOGIN") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Content(
        uiType = uiState.uiType,
        nextScheduleDate = uiState.nextScheduleDate,
        attendance = uiState.attendanceBoardStatus.attendance,
        late = uiState.attendanceBoardStatus.late,
        absent = uiState.attendanceBoardStatus.absent,
        memberAttendances = uiState.memberAttendances,
        editItems = uiState.attendanceStatusList,
        teamList = uiState.teams,
        selectedTeamIndex = uiState.selectedTeamIndex,
        isEditDialogVisible = uiState.isShowEditPopup,
        isShowScreenChangeDropDownVisible = uiState.isShowScreenChangeDropDown,
        isShowScheduleBottomSheet = uiState.isShowScheduleBottomSheet,
        isShowAbsentNotificationPopup = uiState.isShowAbsentNotificationPopup,
        isShowQrScanner = uiState.isShowQrScanner,
        isAttendanceSuccess = uiState.isAttendanceSuccess,
        selectedEditText = uiState.selectedEditText,
        scheduleList = uiState.schedules,
        onTabClick = { teamId, selectedIndex ->
            viewModel.onIntent(AdminIntent.TabChanged(teamId, selectedIndex))
        },
        onEditClick = { selectedEditText, attendanceId, userId ->
            viewModel.onIntent(AdminIntent.ShowEditPopup(selectedEditText, attendanceId, userId))
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
        onHeaderQrClick = {
            viewModel.onIntent(AdminIntent.ShowQrScanner)
        },
        onHeaderProfileClick = {
            viewModel.onIntent(AdminIntent.GoToProfile)
        },
        onScreenChangeDropDownDismiss = {
            viewModel.onIntent(AdminIntent.HideDropDownScreenChange)
        },
        onUiTypeChanged = {
            viewModel.onIntent(AdminIntent.ScreenUiTypeChanged(it))
        },
        onScheduleBottomSheetDismiss = {
            viewModel.onIntent(AdminIntent.HideScheduleBottomSheet)
        },
        onDataClick = {
            viewModel.onIntent(AdminIntent.ShowScheduleBottomSheet)
        },
        onScheduleItemClick = { selectedScheduleId, month, day, index ->
            viewModel.onIntent(AdminIntent.SchedulePositionSelected(selectedScheduleId, month, day, index))
        },
        onAbsentNotificationClick = {
            viewModel.onIntent(AdminIntent.ShowAbsentNotificationPopup)
        },
        onAbsentNotificationDismiss = {
            viewModel.onIntent(AdminIntent.HideAbsentNotificationPopup)
        },
        onQrScannerBottomSheetDismiss = {
            viewModel.onIntent(AdminIntent.HideQrScanner)
        },
        onQrDetected = { qrCode ->
            viewModel.onIntent(AdminIntent.QrDetected(qrCode))
        }
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
    memberAttendances: ImmutableList<AdminScheduleTeamAttendance>,
    scheduleList: ImmutableList<ScheduleUiModel>,
    editItems: ImmutableList<AttendanceStatus>,
    teamList: ImmutableList<AdminTeam>,
    selectedTeamIndex: Int,
    isEditDialogVisible: Boolean,
    isShowScreenChangeDropDownVisible: Boolean,
    isShowScheduleBottomSheet: Boolean,
    isShowAbsentNotificationPopup: Boolean,
    isShowQrScanner: Boolean,
    isAttendanceSuccess: Boolean,
    selectedEditText: String,
    onTabClick: (teamId: Int, selectedIndex: Int) -> Unit,
    onEditClick:(selectedEditText: String, attendanceId: Int, userId: Int) -> Unit,
    onEditConfirm: () -> Unit,
    onEditItemSelected: (String) -> Unit,
    onHeaderClick:() -> Unit,
    onHeaderQrClick: () -> Unit,
    onHeaderProfileClick: () -> Unit,
    onUiTypeChanged: (AdminType) -> Unit,
    onScreenChangeDropDownDismiss: () -> Unit,
    onScheduleBottomSheetDismiss: () -> Unit,
    onDataClick: () -> Unit,
    onScheduleItemClick: (selectedScheduleId: Long, month: Int, day: Int, index: Int) -> Unit,
    onAbsentNotificationClick: () -> Unit,
    onAbsentNotificationDismiss: () -> Unit,
    onQrScannerBottomSheetDismiss: () -> Unit,
    onQrDetected: (qrCode: String) -> Unit
) {
    val headerText =
        if (uiType == AdminType.Attendance) {
            stringResource(R.string.attendance)
        } else stringResource(R.string.schedule)

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
                text = headerText,
                onClick = onHeaderClick,
                onQrClick = onHeaderQrClick,
                onProfileClick = onHeaderProfileClick
            )

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
                            memberAttendances = memberAttendances,
                            teamList = teamList,
                            selectedTeamIndex = selectedTeamIndex,
                            onTabClick = { teamId, selectedIndex ->
                                onTabClick(teamId, selectedIndex)
                            },
                            onEditClick = { selectedEditText, attendanceId, userId ->
                                onEditClick(selectedEditText, attendanceId, userId)
                            },
                            onDataClick = onDataClick,
                            onAbsentNotificationClick = onAbsentNotificationClick
                        )
                    }
                    AdminType.Schedule -> {
                        ScheduleScreen(
                            scheduleList = scheduleList
                        )
                    }
                }
            }
        }

        EditPopup(
            isShow = isEditDialogVisible,
            title = stringResource(R.string.attendance_change_confirm),
            items = editItems,
            selectedText = selectedEditText,
            onConfirm = onEditConfirm,
            onItemSelected = {
                onEditItemSelected(it)
            }
        )

        ScreenChangeDropDown(
            isShow = isShowScreenChangeDropDownVisible,
            onScreenChangeDropDownDismiss = onScreenChangeDropDownDismiss,
            onUiTypeChanged = { onUiTypeChanged(it) }
        )

        ScheduleBottomSheet(
            isShow = isShowScheduleBottomSheet,
            scheduleList = scheduleList,
            onDismiss = onScheduleBottomSheetDismiss,
            onConfirm = {
                onScheduleBottomSheetDismiss()
            },
            onScheduleItemClick = {
                onScheduleItemClick(
                    scheduleList[it].id,
                    scheduleList[it].month,
                    scheduleList[it].day,
                    it
                )
            }
        )

        OneButtonTitleContentPopup(
            isShow = isShowAbsentNotificationPopup,
            titleText = stringResource(R.string.schedule_warning_title),
            contentText = stringResource(R.string.schedule_late_penalty_message)
        ) {
            onAbsentNotificationDismiss()
        }

        QrScanner(
            isShow = isShowQrScanner,
            onDismiss = onQrScannerBottomSheetDismiss,
            isAttendanceSuccess = isAttendanceSuccess,
            onQrCodeDetected = {
                onQrDetected(it)
                Log.d("QrScanner-onQrCodeDetected", it)
            },
            onError = {
                Log.d("QrScanner-onError", it)
            },
            onResult = {
                Log.d("QrScanner-onResult", "$it")
            }
        )
    }
}

@Composable
internal fun EditPopup(
    modifier: Modifier = Modifier,
    isShow: Boolean,
    title: String,
    selectedText: String,
    items: ImmutableList<AttendanceStatus>,
    onConfirm: () -> Unit,
    onItemSelected: (String) -> Unit
) {
    if (!isShow) return

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
                color = BackgroundSecondaryDark
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
                        color = BackgroundSecondaryDark
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

@Composable
internal fun ScheduleBottomSheet(
    modifier: Modifier = Modifier,
    isShow: Boolean,
    scheduleList: ImmutableList<ScheduleUiModel>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onScheduleItemClick: (index: Int) -> Unit
) {
    if (!isShow) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDismiss()
                }
        ) {
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(520.dp)
                    .background(
                        color = White,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .background(
                            color = TextSecondaryDark,
                            shape = RoundedCornerShape(11.dp)
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                DddText(
                    text = stringResource(R.string.select_schedule),
                    style = Typography.titleMediumB,
                    color = BackgroundSecondaryDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(scheduleList) { index, item ->
                        ScheduleCard(
                            month = "${ item.month }월",
                            day = "${ item.day }",
                            title = item.name,
                            description = item.desc,
                            isSelected = item.isSelected
                        ) {
                            onScheduleItemClick(index)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonEnabled
                    ),
                    onClick = onConfirm
                ) {
                    DddText(
                        text = stringResource(R.string.confirm),
                        style = Typography.bodyLargeM
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
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
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderSize = if (isSelected) 1.dp else 0.dp
    val borderColor = if (isSelected) BorderEnabled else BackgroundSecondaryLight
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(shape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .background(
                color = BackgroundSecondaryLight
            )
            .border(
                width = borderSize,
                color = borderColor,
                shape = shape
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .size(54.dp)
                .background(
                    color = NeutralBlue20,
                    shape = shape
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
                color = BackgroundDefault
            )

            DddText(
                text = description,
                style = Typography.bodySmallR,
                color = TextSecondaryLight
            )
        }
    }
}

@Composable
internal fun AbsentNotificationPopup(
    modifier: Modifier = Modifier,
    isShow: Boolean,
    onDismiss: () -> Unit
) {

    if (!isShow) return

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .padding(horizontal = 36.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = BackgroundSecondaryLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DddText(
                    text = stringResource(R.string.schedule_warning_title),
                    style = Typography.titleSmallB,
                    color = BackgroundSecondaryDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                DddText(
                    text = stringResource(R.string.schedule_late_penalty_message),
                    style = Typography.bodySmallR,
                    color = TextSecondaryLight,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonEnabled
                    ),
                    onClick = onDismiss
                ) {
                    DddText(
                        text = stringResource(R.string.confirm),
                        style = Typography.bodySmallM
                    )
                }
            }
        }
    }
}

@Composable
private fun QrScanner(
    modifier: Modifier = Modifier,
    isShow: Boolean,
    isAttendanceSuccess: Boolean,
    onDismiss: () -> Unit,
    onQrCodeDetected: (String) -> Unit,
    onError: (String) -> Unit,
    onResult: (QrScanState) -> Unit
) {
    if (!isShow) return

    val configuration = LocalConfiguration.current
    val sheetHeight = configuration.screenHeightDp.dp * 0.9f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(sheetHeight)
                .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        ) {
            QrScannerScreen(
                onQrCodeDetected = onQrCodeDetected,
                onResult = onResult,
                onError = onError,
                isAttendanceSuccess = isAttendanceSuccess
            )

            Box(
                modifier.padding(start = 24.dp, top = 24.dp)
            ) {
                DddIconButton(
                    modifier = Modifier
                        .size(36.dp),
                    enabledIconRes = R.drawable.ic_qr_close,
                    disabledIconRes = R.drawable.ic_qr_close
                ) {
                    onDismiss()
                }
            }
        }
    }
}