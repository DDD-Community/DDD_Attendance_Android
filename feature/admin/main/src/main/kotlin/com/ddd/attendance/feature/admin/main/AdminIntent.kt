package com.ddd.attendance.feature.admin.main

interface AdminIntent {
    data class TabChanged(val index: Int): AdminIntent
    data object ShowEditPopup: AdminIntent
    data object ConfirmEditPopup: AdminIntent
    data class DropDownTextChanged(val text: String): AdminIntent
}