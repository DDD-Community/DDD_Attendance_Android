package com.ddd.attendance.feature.admin.profile

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AdminProfileUiState(
    val name: String = "",
    val jobRole: String = "",
    val team: String = "",
    val generation: String = "",
    val managerRoles: ImmutableList<String> = persistentListOf(),
    val organization: String = "",
    val version: String = "",
    val privacyPolicyUrl: String = "",
    val privacyPolicyText: String = "",
    val isShowContributorBottomSheet: Boolean = false,
    val isShowWithdrawAccountPopup: Boolean = false,
    val isShowLogoutPopup: Boolean = false,
)