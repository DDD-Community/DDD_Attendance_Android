package com.ddd.attendance.feature.admin.main

interface AdminIntent {
    data class TabChanged(val index: Int): AdminIntent
    data class DropDownTextChanged(val text: String): AdminIntent
    data class ScreenUiTypeChanged(val type: AdminType): AdminIntent
    data object ShowEditPopup: AdminIntent
    data object HideEditPopup: AdminIntent
    data object ShowDropDownScreenChange: AdminIntent
    data object HideDropDownScreenChange: AdminIntent

}