package com.ddd.attendance.feature.admin.main

import com.ddd.attendance.feature.admin.attendance.model.AttendanceStatus
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceInfo
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceType
import com.ddd.attendance.feature.admin.schedule.model.Schedule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AdminUiState(
    val uiType: AdminType = AdminType.Attendance,
    val nextScheduleDate: String = "2026.03.07",
    val selectedTeamIndex: Int = 0,
    val isShowEditPopup: Boolean = false,
    val isShowScreenChangeDropDown: Boolean = false,
    val isShowScheduleBottomSheet: Boolean = false,
    val selectedEditText: String = "출석",
    val dummyAttendanceStatus: AttendanceStatus = AttendanceStatus(
        attendance = 10,
        late = 2,
        absent = 3
    ),
    val dummyMemberAttendanceInfos: ImmutableList<MemberAttendanceInfo> =
        persistentListOf(
            MemberAttendanceInfo(name = "이경서", role = "PM", MemberAttendanceType.NONE),
            MemberAttendanceInfo(name = "이상훈", role = "Android", MemberAttendanceType.ATTENDANCE),
            MemberAttendanceInfo(name = "오세민", role = "Android", MemberAttendanceType.LATE),
            MemberAttendanceInfo(name = "홍영주", role = "Owner", MemberAttendanceType.ABSENT),
            MemberAttendanceInfo(name = "조지원", role = "Backend", MemberAttendanceType.ATTENDANCE),
            MemberAttendanceInfo(name = "이준석", role = "Backend", MemberAttendanceType.NONE),
            MemberAttendanceInfo(name = "홍은표", role = "iOS", MemberAttendanceType.LATE),
            MemberAttendanceInfo(name = "서원지", role = "iOS", MemberAttendanceType.ATTENDANCE)
        ),

    val dummySelectedEditPopupItemList: ImmutableList<String> =
        persistentListOf(
            "출석",
            "지각",
            "결석",
            "발표"
        ),
    val dummyTeamList: ImmutableList<String> = persistentListOf("web 1팀", "web 2팀", "Android 1팀", "Android 2팀", "iOS 1팀", "iOS 2팀"),

    val dummyScheduleList: ImmutableList<Schedule> = persistentListOf(
        Schedule(month = "12월", "21", "오리엔테이션", "커리큘럼에 대한 설명 문구 작성1", false),
        Schedule(month = "12월", "28", "부스팅 데이 1", "커리큘럼에 대한 설명 문구 작성2", false),
        Schedule(month = "1월", "01", "직군 모임 1", "직군별 자료 발표", false),
        Schedule(month = "1월", "08", "오리엔테이션", "커리큘럼에 대한 설명 문구 작성3", false),
        Schedule(month = "1월", "15", "부스팅 데이 2", "커리큘럼에 대한 설명 문구 작성4", false),
        Schedule(month = "1월", "22", "직군 모임 2", "직군 인사이트 공유", false),
        Schedule(month = "1월", "29", "해커톤", "해커톤 진행", false)
    )
)