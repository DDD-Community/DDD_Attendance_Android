package com.ddd.attendance.feature.admin.profile

data class AdminProfileUiState(
    val name: String,
    val position: String,
    val team: String,
    val generation: String,
    val task: String,
    val organization: String,
    val appInfo: AppInfo,
    val isShowContributorBottomSheet: Boolean = false
)