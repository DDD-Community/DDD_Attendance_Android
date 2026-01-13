package com.ddd.attendance.feature.admin.profile

interface AdminProfileIntent {
    data class ShowContributor(val isShow: Boolean): AdminProfileIntent
    data class ShowWithdrawAccount(val isShow: Boolean): AdminProfileIntent
    data class ShowLogout(val isShow: Boolean): AdminProfileIntent
    data object PopBackStack: AdminProfileIntent
}