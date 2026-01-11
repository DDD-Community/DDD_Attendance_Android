package com.ddd.attendance.feature.onboarding

import androidx.annotation.StringRes

enum class OnBoardingStep(
    val index: Int,
    @StringRes val titleRes: Int,
    @StringRes val subTitleRes: Int,
) {
    Invite(
        index = 0,
        titleRes = R.string.enter_invite_code,
        subTitleRes = R.string.invite_code_description
    ),
    Name(
        index = 1,
        titleRes = R.string.enter_name,
        subTitleRes = R.string.name_description
    ),
    Job(
        index = 2,
        titleRes = R.string.select_role,
        subTitleRes = R.string.select_role_description
    ),
    Team(
        index = 3,
        titleRes = R.string.select_team,
        subTitleRes = R.string.select_team_description
    ),
    Role(
        index = 4,
        titleRes = R.string.select_work,
        subTitleRes = R.string.select_work_description
    )
}