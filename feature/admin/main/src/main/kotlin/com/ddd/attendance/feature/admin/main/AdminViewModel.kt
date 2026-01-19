package com.ddd.attendance.feature.admin.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.model.UsersException
import com.ddd.attendance.domain.usecase.AttendanceStatusUseCase
import com.ddd.attendance.domain.usecase.AttendancesChangeUseCase
import com.ddd.attendance.domain.usecase.AttendancesUseCase
import com.ddd.attendance.domain.usecase.DeleteDataStoreWithdrawAccountUseCase
import com.ddd.attendance.domain.usecase.GetAdminScheduleAttendanceUseCase
import com.ddd.attendance.domain.usecase.GetAdminScheduleTeamAttendanceUseCase
import com.ddd.attendance.domain.usecase.GetAdminTeamUseCase
import com.ddd.attendance.domain.usecase.GetScheduleUseCase
import com.ddd.attendance.domain.usecase.LogoutUseCase
import com.ddd.attendance.feature.admin.attendance.model.AttendanceBoardStatus
import com.ddd.attendance.feature.admin.schedule.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getAdminScheduleTeamAttendanceUseCase: GetAdminScheduleTeamAttendanceUseCase,
    private val getAdminScheduleAttendanceUseCase: GetAdminScheduleAttendanceUseCase,
    private val deleteDataStoreWithdrawAccountUseCase: DeleteDataStoreWithdrawAccountUseCase,
    private val getAdminTeamUseCase: GetAdminTeamUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val attendancesUseCase: AttendancesUseCase,
    private val attendancesChangeUseCase: AttendancesChangeUseCase,
    private val attendanceStatusUseCase: AttendanceStatusUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AdminNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val schedulesFlow = getScheduleUseCase()
                .map { schedules ->
                    val today = LocalDate.now()
                    val scheduleId = schedules.findNextSchedule(today)?.id ?: 0
                    val nextScheduleDate = schedules.findNextSchedule(today)?.toFormattedDate(today.year).orEmpty()
                    Triple(
                        schedules.toUi().toPersistentList(),
                        scheduleId,
                        nextScheduleDate
                    )
                }

            val teamsFlow = getAdminTeamUseCase()
            val attendanceStatusFlow = attendanceStatusUseCase()

            val selectedScheduleIdFlow = uiState.map { it.selectedScheduleId }
            val selectedTeamIdFlow = uiState.map { it.selectedTeamId }

            val scheduleAttendancesFlow = combine(selectedScheduleIdFlow, selectedTeamIdFlow) { scheduleId, teamId ->
                scheduleId to teamId
            }.distinctUntilChanged()
                .filter { (scheduleId, teamId) -> scheduleId > 0 && teamId > 0 }
                .flatMapLatest { (scheduleId, teamId) ->
                    getAdminScheduleTeamAttendanceUseCase(scheduleId.toInt(), teamId)
                }

            val scheduleBoardFlow = selectedScheduleIdFlow
                .distinctUntilChanged()
                .filter { it > 0 }
                .flatMapLatest { scheduleId ->
                    getAdminScheduleAttendanceUseCase(scheduleId.toInt())
                }

            // 모든 Flow combine
            combine(
                schedulesFlow,
                teamsFlow,
                attendanceStatusFlow,
                scheduleAttendancesFlow,
                scheduleBoardFlow
            ) { schedulesData, teams, attendanceStatus, memberAttendances, attendanceBoardStatus ->
                val (schedules, selectedScheduleId, nextScheduleDate) = schedulesData
                _uiState.value.copy(
                    schedules = schedules,
                    nextScheduleDate = nextScheduleDate,
                    selectedScheduleId = selectedScheduleId,
                    teams = teams.toPersistentList(),
                    attendanceStatusList = attendanceStatus.toPersistentList(),
                    memberAttendances = memberAttendances.toPersistentList(),
                    attendanceBoardStatus = AttendanceBoardStatus(
                        attendance = attendanceBoardStatus.attended,
                        late = attendanceBoardStatus.late,
                        absent = attendanceBoardStatus.absent
                    )
                )
            }.catch { throwable ->
                if (throwable is UsersException.Unauthorized) {
                    onLogout()
                }
            }.collect {
                _uiState.value = it
            }
        }
    }

    private fun attendanceChange(
        attendanceId: Long,
        scheduleId: Long,
        status: String,
        userId: Long
    ) {
        val state = _uiState.value

        viewModelScope.launch {
            attendancesChangeUseCase(
                attendanceId = attendanceId,
                scheduleId = scheduleId,
                status = status,
                userId = userId
            )
                .flatMapConcat {
                    getAdminScheduleTeamAttendanceUseCase(
                        scheduleId = state.selectedScheduleId.toInt(),
                        teamId = state.selectedTeamId
                    )
                }
                .catch { e ->
                    Log.e("AttendanceChange", "출석 상태 변경 실패", e)
                    // 실패하면 롤백 가능
                    _uiState.update { it.copy(memberAttendances = state.memberAttendances) }
                }
                .collect { latestList ->
                    _uiState.update { it.copy(memberAttendances = latestList.toImmutableList()) }
                }
        }
    }

    private fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
                .onEach {
                    deleteDataStoreWithdrawAccountUseCase(isLogout = true)
                    _navigationEvent.emit(AdminNavigationEvent.GoToLogin)
                }
                .collect()
        }
    }

    fun onIntent(intent: AdminIntent) {
        when(intent) {
            is AdminIntent.GoToProfile -> goToProfile()
            is AdminIntent.QrDetected -> {
                viewModelScope.launch {
                    attendancesUseCase(qrCode = intent.qrCode)
                        .onEach {
                            _uiState.update { state ->
                                state.copy(isAttendanceSuccess = state.isAttendanceSuccess)
                            }
                        }
                        .catch {

                        }
                        .collect()
                }
            }
            else -> {
                _uiState.update {
                    reduce(it, intent)
                }
            }
        }
    }

    private fun reduce(
        state: AdminUiState,
        intent: AdminIntent
    ): AdminUiState {
        return when (intent) {
            is AdminIntent.TabChanged -> {
                state.copy(selectedTeamId = intent.teamId, selectedTeamIndex = intent.selectedIndex)
            }
            is AdminIntent.DropDownTextChanged -> state.copy(selectedEditText = intent.text)
            is AdminIntent.ScreenUiTypeChanged -> state.copy(uiType = intent.type)
            is AdminIntent.SchedulePositionSelected -> {
                val newList = state.schedules.mapIndexed { index, item ->
                    if (index == intent.index) item.copy(isSelected = true)
                    else item.copy(isSelected = false)
                }

                val today = LocalDate.now()
                val newDate = "%04d.%02d.%02d".format(
                    today.year,
                    intent.month,
                    intent.day
                )

                state.copy(
                    nextScheduleDate = newDate,
                    selectedScheduleId = intent.selectedScheduleId,
                    schedules = newList.toImmutableList()
                )
            }

            is AdminIntent.ShowEditPopup -> {
                state.copy(
                    isShowEditPopup = true,
                    selectedUserId = intent.userId,
                    selectedAttendanceId = intent.attendanceId,
                    selectedEditText = intent.selectedEditText
                )
            }

            is AdminIntent.HideEditPopup -> {
                attendanceChange(
                    attendanceId = state.selectedAttendanceId.toLong(),
                    userId = state.selectedUserId.toLong(),
                    scheduleId = state.selectedScheduleId,
                    status = when(state.selectedEditText) {
                        "출석" -> "ATTENDED"
                        "지각" -> "LATE"
                        "결석" -> "ABSENT"
                        else -> "NONE"
                    }
                )

                state.copy(isShowEditPopup = false)
            }

            is AdminIntent.ShowDropDownScreenChange -> state.copy(isShowScreenChangeDropDown = true)
            is AdminIntent.HideDropDownScreenChange -> state.copy(isShowScreenChangeDropDown = false)
            is AdminIntent.ShowScheduleBottomSheet -> state.copy(isShowScheduleBottomSheet = true)
            is AdminIntent.HideScheduleBottomSheet -> state.copy(isShowScheduleBottomSheet = false)
            is AdminIntent.ShowAbsentNotificationPopup -> state.copy(isShowAbsentNotificationPopup = true)
            is AdminIntent.HideAbsentNotificationPopup -> state.copy(isShowAbsentNotificationPopup = false)
            is AdminIntent.ShowQrScanner -> state.copy(isShowQrScanner = true)
            is AdminIntent.HideQrScanner -> state.copy(isShowQrScanner = false, isAttendanceSuccess = false)
            else -> state
        }
    }

    private fun goToProfile() {
        viewModelScope.launch { _navigationEvent.emit(AdminNavigationEvent.GoToProfile) }
    }

    private fun Schedule.toFormattedDate(year: Int): String =
        "%04d.%02d.%02d".format(
            year,
            month,
            day
        )

    private fun List<Schedule>.findNextSchedule(today: LocalDate): Schedule? =
        this.mapNotNull { item ->
            runCatching {
                LocalDate.of(
                    today.year,
                    item.month,
                    item.day
                ) to item
            }.getOrNull()
        }
            .filter { (date, _) -> // 오늘과 오늘 이후 일정만 남긴다
                !date.isBefore(today)
            }
            .minByOrNull { (date, _) -> // 오늘과의 날짜 차이가 가장 작은 일정 선택
                ChronoUnit.DAYS.between(today, date)
            }
            ?.second
}