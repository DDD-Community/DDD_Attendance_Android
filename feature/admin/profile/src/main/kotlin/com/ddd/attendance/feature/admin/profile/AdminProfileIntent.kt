package com.ddd.attendance.feature.admin.profile

interface AdminProfileIntent {
    data object ShowContributorBottomSheet: AdminProfileIntent
    data object HideContributorBottomSheet: AdminProfileIntent
    data object PopBackStack: AdminProfileIntent
}