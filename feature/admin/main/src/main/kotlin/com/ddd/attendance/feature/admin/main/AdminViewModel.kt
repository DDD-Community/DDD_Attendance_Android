package com.ddd.attendance.feature.admin.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.usecase.AttendancesChangeUseCase
import com.ddd.attendance.domain.usecase.AttendancesUseCase
import com.ddd.attendance.domain.usecase.GetAdminScheduleAttendanceUseCase
import com.ddd.attendance.domain.usecase.GetAdminScheduleTeamAttendanceUseCase
import com.ddd.attendance.domain.usecase.GetAdminTeamUseCase
import com.ddd.attendance.domain.usecase.GetScheduleUseCase
import com.ddd.attendance.feature.admin.attendance.model.AttendanceStatus
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
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
    private val getAdminTeamUseCase: GetAdminTeamUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val attendancesUseCase: AttendancesUseCase,
    private val attendancesChangeUseCase: AttendancesChangeUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        observeSchedules()
        observeTeams()
        observeScheduleAttendances()
        observeScheduleTeamAttendances()
    }

    private fun observeScheduleTeamAttendances() {
        uiState
            .map { it.selectedScheduleId to it.selectedTeamId }
            .distinctUntilChanged()
            .filter { (scheduleId, teamId) ->
                scheduleId > 0 && teamId > 0
            }
            .flatMapLatest { (scheduleId, teamId) ->
                getAdminScheduleTeamAttendanceUseCase(scheduleId.toInt(), teamId)
            }
            .onEach { attendances ->
                _uiState.update { state ->
                    state.copy(
                        memberAttendances = attendances.toPersistentList()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeScheduleAttendances() {
        uiState
            .map { it.selectedScheduleId }
            .distinctUntilChanged()
            .filter { it > 0 } // 초기값 방어 (중요)
            .flatMapLatest { scheduleId ->
                getAdminScheduleAttendanceUseCase(scheduleId.toInt())
            }
            .onEach { item ->
                _uiState.update { state ->
                    state.copy(
                        attendanceStatus = AttendanceStatus(
                            attendance = item.attended,
                            late = item.late,
                            absent = item.absent
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeSchedules() {
        getScheduleUseCase()
            .onEach { schedules ->
                val today = LocalDate.now()

                val scheduleId = schedules.findNextSchedule(today)?.id?: 0

                val nextScheduleDate = schedules
                    .findNextSchedule(today)
                    ?.toFormattedDate(today.year)
                    .orEmpty()

                _uiState.update { state ->
                    state.copy(
                        schedules = schedules.toUi().toPersistentList(),
                        nextScheduleDate = nextScheduleDate,
                        selectedScheduleId = scheduleId
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeTeams() {
        getAdminTeamUseCase()
            .onEach { teams ->
                _uiState.update { state ->
                    // selectedTeamId가 이미 세팅되어 있으면 그대로 사용
                    val currentSelectedTeamId = state.selectedTeamId.takeIf { it != 0 } ?: teams[0].teamId
                    state.copy(
                        selectedTeamId = currentSelectedTeamId,
                        teams = teams.toPersistentList()
                    )
                }
            }
            .launchIn(viewModelScope)
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

            is AdminIntent.ShowEditPopup -> state.copy(
                isShowEditPopup = true,
                selectedEditText = intent.selectedText
            )

            is AdminIntent.HideEditPopup -> {
                Log.d("AdminViewModel-Data-Reduce", state.selectedEditText)
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
        viewModelScope.launch { _navigationEvent.emit(NavigationEvent.GoToProfile) }
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