package com.ddd.attendance.feature.admin

interface AdminIntent {
    data class TabChanged(val index: Int): AdminIntent
}