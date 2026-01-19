package com.ddd.attendance.feature.member.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.model.UsersException
import com.ddd.attendance.domain.repository.UserRepository
import com.ddd.attendance.domain.usecase.DeleteDataStoreWithdrawAccountUseCase
import com.ddd.attendance.domain.usecase.GetScheduleUseCase
import com.ddd.attendance.domain.usecase.GetUserInfoUseCase
import com.ddd.attendance.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AttendanceStats(
    val attendance: Int,
    val late: Int,
    val absent: Int
)

data class ScheduleItem(
    val date: String,
    val title: String,
    val subtitle: String,
    val status: String = ""
)

data class MemberMainUiState(
    val memberName: String,
    val activityPeriod: String,
    val attendanceStats: AttendanceStats,
    val scheduleItems: List<ScheduleItem>,
    val generationNumber: Int
)

@HiltViewModel
class MemberMainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteDataStoreWithdrawAccountUseCase: DeleteDataStoreWithdrawAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MemberMainUiState(
            memberName = "김디디",
            activityPeriod = "2025.03.12 - 2025.08.12",
            attendanceStats = AttendanceStats(
                attendance = 8,
                late = 2,
                absent = 1
            ),
            generationNumber = 12,
            scheduleItems = listOf(
                ScheduleItem(
                    date = "12월\n99",
                    title = "오리엔테이션",
                    subtitle = "커리큘럼에 대한 설명 문구 작성",
                    status = "ATTENDANCE"
                ),
                ScheduleItem(
                    date = "12월\n9",
                    title = "부스팅 데이1",
                    subtitle = "커리큘럼에 대한 설명문구 작성",
                    status = "LATE"
                ),
                ScheduleItem(
                    date = "12월\n99",
                    title = "직군 모임1",
                    subtitle = "커리큘럼에 대한 설명 문구 작성",
                    status = "ABSENT"
                ),
                ScheduleItem(
                    date = "12월\n99",
                    title = "오리엔테이션",
                    subtitle = "커리큘럼에 대한 설명 문구 작성",
                    status = ""
                )
            )
        )
    )

    val uiState: StateFlow<MemberMainUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MemberNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        // 임시 accessToken 저장 (테스트용)
//        viewModelScope.launch {
//            userRepository.saveAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OCIsImlhdCI6MTc2ODI5MTU5MCwiZXhwIjoxNzY4Mjk1MTkwLCJyb2xlIjoiTUVNQkVSIn0.cUND7w8m8ZWlHpMt2D3EfSQaoY3F3DAAOeEQs22bLdk")
//        }
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {

            userRepository.getMe()

            userRepository.fetchActivitySchedules()

            val schedulesFlow = getScheduleUseCase()
                .map { list -> list.map { it.toScheduleItem() } }

            val activityPeriodFlow = userRepository.getActivityPeriod()
            val userInfoFlow = getUserInfoUseCase()

            combine(schedulesFlow, activityPeriodFlow, userInfoFlow) { scheduleItems, activityPeriod, userInfo ->
                _uiState.value.copy(
                    memberName = userInfo.name,
                    generationNumber = extractGenerationNumber(userInfo.generation),
                    activityPeriod = activityPeriod.ifEmpty { _uiState.value.activityPeriod },
                    attendanceStats = AttendanceStats(
                        attendance = userInfo.attendanceCount,
                        late = userInfo.lateCount,
                        absent = userInfo.absentCount
                    ),
                    scheduleItems = _uiState.value.scheduleItems + scheduleItems
                )

            }.catch { throwable ->
                if (throwable is UsersException.Unauthorized) {
                    onLogout()
                }
            }.collect { updatedState ->
                _uiState.value = updatedState
            }
        }
    }

    private fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
                .onEach {
                    deleteDataStoreWithdrawAccountUseCase(isLogout = true)
                    _navigationEvent.emit(MemberNavigationEvent.GoToLogin)
                }
                .collect()
        }
    }

    private fun extractGenerationNumber(generation: String): Int {
        if (generation.isEmpty()) return 0
        // "13기" -> 13, "13" -> 13 등의 형식에서 숫자만 추출
        return generation.filter { it.isDigit() }.toIntOrNull() ?: 0
    }

    private fun Schedule.toScheduleItem(): ScheduleItem {
        val monthStr = "${month}월"
        val dayStr = String.format("%02d", day)
        return ScheduleItem(
            date = "$monthStr\n$dayStr",
            title = name,
            subtitle = desc,
            status = status
        )
    }
}