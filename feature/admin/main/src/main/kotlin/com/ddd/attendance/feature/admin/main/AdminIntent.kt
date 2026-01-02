package com.ddd.attendance.feature.admin.main

interface AdminIntent {
    data class TabChanged(val index: Int): AdminIntent
    data object ShowEditPopup: AdminIntent
    data object DismissEditPopup: AdminIntent
}