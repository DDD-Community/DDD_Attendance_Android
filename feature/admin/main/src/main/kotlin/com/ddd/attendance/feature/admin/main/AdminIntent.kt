package com.ddd.attendance.feature.admin.main

sealed interface AdminIntent {
    data class TabChanged(val teamId: Int, val selectedIndex: Int): AdminIntent
    data class DropDownTextChanged(val text: String): AdminIntent
    data class ScreenUiTypeChanged(val type: AdminType): AdminIntent
    data class ShowEditPopup(val selectedEditText: String, val attendanceId: Int, val userId: Int): AdminIntent
    data class SchedulePositionSelected(val selectedScheduleId: Long, val month: Int, val day: Int, val index: Int): AdminIntent
    data class QrDetected(val qrCode: String): AdminIntent
    data object HideEditPopup: AdminIntent
    data object ShowDropDownScreenChange: AdminIntent
    data object HideDropDownScreenChange: AdminIntent
    data object ShowScheduleBottomSheet: AdminIntent
    data object HideScheduleBottomSheet: AdminIntent
    data object ShowAbsentNotificationPopup: AdminIntent
    data object HideAbsentNotificationPopup: AdminIntent
    data object ShowQrScanner: AdminIntent
    data object HideQrScanner: AdminIntent
    data object GoToProfile: AdminIntent
}