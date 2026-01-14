package com.ddd.attendance.feature.member.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.repository.UserRepository
import com.ddd.attendance.domain.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val getUserInfoUseCase: GetUserInfoUseCase
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

    init {
        // 임시 accessToken 저장 (테스트용)
//        viewModelScope.launch {
//            userRepository.saveAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OCIsImlhdCI6MTc2ODI5MTU5MCwiZXhwIjoxNzY4Mjk1MTkwLCJyb2xlIjoiTUVNQkVSIn0.cUND7w8m8ZWlHpMt2D3EfSQaoY3F3DAAOeEQs22bLdk")
//        }
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            // 먼저 me API를 호출하여 사용자 데이터를 최신 상태로 업데이트
            userRepository.getMe()

            // 활동 스케줄 데이터 가져오기 (활동 기간 계산)
            userRepository.fetchActivitySchedules()

            // 스케줄 데이터 가져오기
            val schedulesResult = userRepository.getSchedules()
            val scheduleItems = schedulesResult.getOrNull()?.let { schedules ->
                schedules.map { it.toScheduleItem() }
            } ?: emptyList()

            // 활동 기간 가져오기
            userRepository.getActivityPeriod().collect { activityPeriod ->
                // API 호출 후 DataStore에서 데이터를 가져와서 UI 업데이트
                getUserInfoUseCase().collect { userInfo ->
                    _uiState.value = _uiState.value.copy(
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
                }
            }
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