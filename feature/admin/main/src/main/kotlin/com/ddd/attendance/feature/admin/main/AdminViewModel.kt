package com.ddd.attendance.feature.admin.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.usecase.AttendanceStatusUseCase
import com.ddd.attendance.domain.usecase.AttendancesChangeUseCase
import com.ddd.attendance.domain.usecase.AttendancesUseCase
import com.ddd.attendance.domain.usecase.DeleteDataStoreWithdrawAccountUseCase
import com.ddd.attendance.domain.usecase.GetAdminScheduleAttendanceUseCase
import com.ddd.attendance.domain.usecase.GetAdminScheduleTeamAttendanceUseCase
import com.ddd.attendance.domain.usecase.GetAdminTeamUseCase
import com.ddd.attendance.domain.usecase.GetScheduleUseCase
import com.ddd.attendance.domain.usecase.GetVotesUseCase
import com.ddd.attendance.domain.usecase.LogoutUseCase
import com.ddd.attendance.domain.model.admin.AdminTeam
import com.ddd.attendance.domain.model.attendance.AttendanceStatus
import com.ddd.attendance.feature.admin.attendance.model.AttendanceBoardStatus
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceType
import com.ddd.attendance.feature.admin.schedule.model.ScheduleUiModel
import com.ddd.attendance.feature.admin.schedule.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
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
    private val logoutUseCase: LogoutUseCase,
    private val getVotesUseCase: GetVotesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AdminNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        observeScheduleTeamAttendances()
        observeScheduleAttendances()
        observeAllData()
        observeVoteAvailability()
    }

    private fun observeVoteAvailability() {
        viewModelScope.launch {
            getVotesUseCase()
                .onEach { votes ->
                    _uiState.update {
                        it.copy(isVoteMenuVisible = votes.isNotEmpty() || DEV_ALWAYS_SHOW_VOTE_MENU)
                    }
                }
                .catch { /* Flow 종료 방지 */ }
                .collect()
        }
    }

    private fun observeAllData() {
        viewModelScope.launch {
            combine(
                attendanceStatusUseCase().map { it.toPersistentList() },
                getScheduleUseCase().map { schedules ->
                    val today = LocalDate.now()
                    val nextSchedule = schedules.findNextSchedule(today)
                    val selectedScheduleId = nextSchedule?.id
                        ?: schedules.firstOrNull()?.id ?: 0
                    val nextScheduleDate = nextSchedule
                        ?.toFormattedDate(today.year)
                        .orEmpty()

                    ScheduleData(
                        schedules = schedules.toUi().toPersistentList(),
                        selectedScheduleId = selectedScheduleId,
                        nextScheduleDate = nextScheduleDate
                    )
                },
                getAdminTeamUseCase().map { it.toPersistentList() }
            ) { attendanceStatusList, scheduleData, teams ->
                CombinedData(attendanceStatusList, scheduleData, teams)
            }
                .catch { /* Flow 종료 방지 */ }
                .collect { (attendanceStatusList, scheduleData, teams) ->
                    _uiState.update { currentState ->
                        val currentSelectedTeamId =
                            if (currentState.selectedTeamId != 0) {
                                currentState.selectedTeamId
                            } else {
                                teams.firstOrNull()?.teamId ?: 0
                            }

                        currentState.copy(
                            attendanceStatusList = attendanceStatusList,
                            schedules = scheduleData.schedules,
                            nextScheduleDate = scheduleData.nextScheduleDate,
                            selectedTeamId = currentSelectedTeamId,
                            selectedScheduleId = scheduleData.selectedScheduleId,
                            teams = teams.toPersistentList()
                        )
                    }
                }
        }
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
                    state.copy(memberAttendances = attendances.toPersistentList())
                }
            }
            .catch { /* Flow 종료 방지 */ }
            .launchIn(viewModelScope)
    }

    private fun observeScheduleAttendances() {
        uiState
            .map { it.selectedScheduleId }
            .distinctUntilChanged()
            .filter { it > 0 }
            .flatMapLatest { scheduleId ->
                getAdminScheduleAttendanceUseCase(scheduleId.toInt())
            }
            .onEach { item ->
                _uiState.update { state ->
                    state.copy(
                        attendanceBoardStatus = AttendanceBoardStatus(
                            attendance = item.attended,
                            late = item.late,
                            absent = item.absent
                        )
                    )
                }
            }
            .catch { /* Flow 종료 방지 */ }
            .launchIn(viewModelScope)
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
                .flatMapConcat { latestList ->
                    _uiState.update { it.copy(memberAttendances = latestList.toImmutableList()) }
                    getAdminScheduleAttendanceUseCase(state.selectedScheduleId.toInt())
                }
                .catch {
                    _uiState.update { it.copy(memberAttendances = state.memberAttendances) }
                }
                .collect { item ->
                    _uiState.update {
                        it.copy(
                            attendanceBoardStatus = AttendanceBoardStatus(
                                attendance = item.attended,
                                late = item.late,
                                absent = item.absent
                            )
                        )
                    }
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
        when (intent) {
            is AdminIntent.GoToProfile -> goToProfile()

            is AdminIntent.QrDetected -> handleQrDetected(intent.qrCode)

            is AdminIntent.HideEditPopup -> {
                val currentState = _uiState.value
                val status = MemberAttendanceType.entries
                    .firstOrNull { it.displayName == currentState.selectedEditText }
                    ?.type ?: "NONE"

                attendanceChange(
                    attendanceId = currentState.selectedAttendanceId.toLong(),
                    userId = currentState.selectedUserId.toLong(),
                    scheduleId = currentState.selectedScheduleId,
                    status = status
                )
                _uiState.update { reduce(it, intent) }
            }

            is AdminIntent.TabChanged,
            is AdminIntent.DropDownTextChanged,
            is AdminIntent.ScreenUiTypeChanged,
            is AdminIntent.SchedulePositionSelected,
            is AdminIntent.ShowEditPopup,
            is AdminIntent.ShowDropDownScreenChange,
            is AdminIntent.HideDropDownScreenChange,
            is AdminIntent.ShowScheduleBottomSheet,
            is AdminIntent.HideScheduleBottomSheet,
            is AdminIntent.ShowAbsentNotificationPopup,
            is AdminIntent.HideAbsentNotificationPopup,
            is AdminIntent.ShowQrScanner,
            is AdminIntent.HideQrScanner -> {
                _uiState.update { reduce(it, intent) }
            }
        }
    }

    private fun handleQrDetected(qrCode: String) {
        viewModelScope.launch {
            attendancesUseCase(qrCode = qrCode)
                .onEach {
                    _uiState.update { state ->
                        state.copy(isAttendanceSuccess = true)
                    }
                    delay(1000L)
                    _uiState.update { state ->
                        state.copy(
                            isShowQrScanner = false,
                            isAttendanceSuccess = false
                        )
                    }
                    fetchAttendanceData()
                }
                .catch {
                    _navigationEvent.emit(AdminNavigationEvent.OnQrApiErrorMessage(it))
                }
                .collect()
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

            is AdminIntent.HideEditPopup -> state.copy(isShowEditPopup = false)
            is AdminIntent.ShowDropDownScreenChange -> state.copy(isShowScreenChangeDropDown = true)
            is AdminIntent.HideDropDownScreenChange -> state.copy(isShowScreenChangeDropDown = false)
            is AdminIntent.ShowScheduleBottomSheet -> state.copy(isShowScheduleBottomSheet = true)
            is AdminIntent.HideScheduleBottomSheet -> state.copy(isShowScheduleBottomSheet = false)
            is AdminIntent.ShowAbsentNotificationPopup -> state.copy(isShowAbsentNotificationPopup = true)
            is AdminIntent.HideAbsentNotificationPopup -> state.copy(isShowAbsentNotificationPopup = false)
            is AdminIntent.ShowQrScanner -> state.copy(isShowQrScanner = true)
            is AdminIntent.HideQrScanner -> state.copy(isShowQrScanner = false, isAttendanceSuccess = false)

            is AdminIntent.GoToProfile,
            is AdminIntent.QrDetected -> state
        }
    }

    private fun fetchAttendanceData() {
        val state = _uiState.value
        if (state.selectedScheduleId <= 0 || state.selectedTeamId <= 0) return

        viewModelScope.launch {
            getAdminScheduleTeamAttendanceUseCase(
                scheduleId = state.selectedScheduleId.toInt(),
                teamId = state.selectedTeamId
            )
                .onEach { attendances ->
                    _uiState.update { it.copy(memberAttendances = attendances.toPersistentList()) }
                }
                .catch { /* 에러 시 기존 목록 유지 */ }
                .collect()
        }
        viewModelScope.launch {
            getAdminScheduleAttendanceUseCase(state.selectedScheduleId.toInt())
                .onEach { item ->
                    _uiState.update {
                        it.copy(
                            attendanceBoardStatus = AttendanceBoardStatus(
                                attendance = item.attended,
                                late = item.late,
                                absent = item.absent
                            )
                        )
                    }
                }
                .catch { /* 에러 시 기존 상태 유지 */ }
                .collect()
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

    private data class ScheduleData(
        val schedules: ImmutableList<ScheduleUiModel>,
        val selectedScheduleId: Long,
        val nextScheduleDate: String
    )

    private data class CombinedData(
        val attendanceStatusList: ImmutableList<AttendanceStatus>,
        val scheduleData: ScheduleData,
        val teams: ImmutableList<AdminTeam>
    )

    companion object {
        // TODO: 임시 개발용 - 테스트 동안 getVotes()가 비어도 '투표' 메뉴 강제 노출. 정식 배포 시 false로.
        private const val DEV_ALWAYS_SHOW_VOTE_MENU = true
    }
}
