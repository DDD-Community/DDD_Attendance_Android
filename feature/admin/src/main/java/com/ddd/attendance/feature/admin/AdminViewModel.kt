package com.ddd.attendance.feature.admin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AdminViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    fun onIntent(intent: AdminIntent) {
        when(intent) {
            is AdminIntent.TabChanged -> {
                _uiState.update {
                    reduce(it, intent)
                }
            }
        }
    }

    private fun reduce(
        state: AdminUiState,
        intent: AdminIntent
    ): AdminUiState {
        return when (intent) {
            is AdminIntent.TabChanged ->
                state.copy(selectedTeamIndex = intent.index)

            else -> state
        }
    }
}