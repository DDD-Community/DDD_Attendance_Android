package com.ddd.attendance.feature.member.vote

sealed interface VoteIntent {
    // STEP 1
    data class TeamToggled(val categoryId: String, val teamId: Int) : VoteIntent
    data class ReasonChanged(val categoryId: String, val text: String) : VoteIntent
    data object GoToStep2 : VoteIntent

    // STEP 2
    data class OptionToggled(val questionId: String, val optionId: String) : VoteIntent
    data class TextChanged(val questionId: String, val text: String) : VoteIntent
    data class BoolSelected(val questionId: String, val value: Boolean) : VoteIntent
    data object Submit : VoteIntent

    // 공통
    data object Retry : VoteIntent
    data object BackPressed : VoteIntent
    data object ConfirmExit : VoteIntent
    data object DismissExit : VoteIntent
    data object ToastShown : VoteIntent
}
