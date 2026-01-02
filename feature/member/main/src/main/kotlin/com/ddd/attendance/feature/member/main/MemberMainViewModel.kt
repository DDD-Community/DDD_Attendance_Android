package com.ddd.attendance.feature.member.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AttendanceStats(
    val attendance: Int,
    val late: Int,
    val absent: Int
)

data class ScheduleItem(
    val date: String,
    val title: String,
    val subtitle: String
)

data class MemberMainUiState(
    val memberName: String,
    val activityPeriod: String,
    val attendanceStats: AttendanceStats,
    val scheduleItems: List<ScheduleItem>,
    val generationNumber: Int
)

@HiltViewModel
class MemberMainViewModel @Inject constructor() : ViewModel() {
    
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
                    date = "12월\n01",
                    title = "오리엔테이션",
                    subtitle = "DDD 12기 첫 만남과 소개"
                ),
                ScheduleItem(
                    date = "12월\n08",
                    title = "부스터 데이 1",
                    subtitle = "개발 환경 세팅 및 기초 학습"
                ),
                ScheduleItem(
                    date = "12월\n15",
                    title = "작곡 모임 1",
                    subtitle = "팀 프로젝트 아이디어 브레인스토밍"
                ),
                ScheduleItem(
                    date = "12월\n22",
                    title = "중간 발표",
                    subtitle = "프로젝트 진행 상황 공유"
                ),
                ScheduleItem(
                    date = "12월\n01",
                    title = "오리엔테이션",
                    subtitle = "DDD 12기 첫 만남과 소개"
                ),
                ScheduleItem(
                    date = "12월\n08",
                    title = "부스터 데이 1",
                    subtitle = "개발 환경 세팅 및 기초 학습"
                ),
                ScheduleItem(
                    date = "12월\n15",
                    title = "작곡 모임 1",
                    subtitle = "팀 프로젝트 아이디어 브레인스토밍"
                ),
                ScheduleItem(
                    date = "12월\n22",
                    title = "중간 발표",
                    subtitle = "프로젝트 진행 상황 공유"
                ),
                ScheduleItem(
                    date = "12월\n01",
                    title = "오리엔테이션",
                    subtitle = "DDD 12기 첫 만남과 소개"
                ),
                ScheduleItem(
                    date = "12월\n08",
                    title = "부스터 데이 1",
                    subtitle = "개발 환경 세팅 및 기초 학습"
                ),
                ScheduleItem(
                    date = "12월\n15",
                    title = "작곡 모임 1",
                    subtitle = "팀 프로젝트 아이디어 브레인스토밍"
                ),
                ScheduleItem(
                    date = "12월\n22",
                    title = "중간 발표",
                    subtitle = "프로젝트 진행 상황 공유"
                )
            )
        )
    )
    
    val uiState: StateFlow<MemberMainUiState> = _uiState.asStateFlow()
}