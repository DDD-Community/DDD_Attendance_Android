package com.ddd.attendance.feature.admin.main

import com.ddd.attendance.feature.admin.attendance.model.AttendanceStatus
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceInfo
import com.ddd.attendance.feature.admin.attendance.model.MemberAttendanceType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AdminUiState(
    val uiType: AdminType = AdminType.Attendance,
    val nextScheduleDate: String = "2026.03.07",
    val selectedTeamIndex: Int = 0,
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

    val dummyTeamList: ImmutableList<String> = persistentListOf("web 1팀", "web 2팀", "Android 1팀", "Android 2팀", "iOS 1팀", "iOS 2팀")
)