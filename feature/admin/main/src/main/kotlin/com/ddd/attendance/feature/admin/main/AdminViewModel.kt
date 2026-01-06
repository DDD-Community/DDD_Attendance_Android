package com.ddd.attendance.feature.admin.main

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
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
        _uiState.update {
            reduce(it, intent)
        }
    }

    private fun reduce(
        state: AdminUiState,
        intent: AdminIntent
    ): AdminUiState {
        return when (intent) {
            is AdminIntent.TabChanged -> state.copy(selectedTeamIndex = intent.index)
            is AdminIntent.DropDownTextChanged -> state.copy(selectedEditText = intent.text)
            is AdminIntent.ScreenUiTypeChanged -> state.copy(uiType = intent.type)
            is AdminIntent.SchedulePositionSelected -> {
                val newList = state.dummyScheduleList.mapIndexed { index, item ->
                    if (index == intent.index) item.copy(isSelected = true)
                    else item.copy(isSelected = false)
                }
                state.copy(
                    dummyScheduleList = newList.toImmutableList()
                )
            }
            is AdminIntent.ShowEditPopup -> state.copy(
                isShowEditPopup = true,
                selectedEditText = intent.selectedText
            )
            is AdminIntent.HideEditPopup -> {
                Log.d("AdminViewModel-Data-Reduce", state.selectedEditText)
                state.copy(isShowEditPopup = false)
            }
            is AdminIntent.ShowDropDownScreenChange -> state.copy(isShowScreenChangeDropDown = true)
            is AdminIntent.HideDropDownScreenChange -> state.copy(isShowScreenChangeDropDown = false)
            is AdminIntent.ShowScheduleBottomSheet -> state.copy(isShowScheduleBottomSheet = true)
            is AdminIntent.HideScheduleBottomSheet -> state.copy(isShowScheduleBottomSheet = false)

            else -> state
        }
    }
}