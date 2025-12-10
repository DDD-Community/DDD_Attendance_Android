package com.ddd.attendance.feature.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState.asStateFlow()

    fun onIntent(intent: OnBoardingIntent) {
        _uiState.update { state ->
            reduce(state, intent)
        }
    }

    private fun reduce(
        state: OnBoardingUiState,
        intent: OnBoardingIntent
    ): OnBoardingUiState {

        val stepBlock = when (intent) {
            OnBoardingIntent.BackStepBlock -> (state.step - 1).coerceAtLeast(0)
            OnBoardingIntent.NextStepBlock -> (state.step + 1).coerceAtMost(3)
        }

        return state.copy(
            step = stepBlock,
            whiteBlockCount = stepBlock,
            blackBlockCount = 3 - stepBlock
        )
    }
}