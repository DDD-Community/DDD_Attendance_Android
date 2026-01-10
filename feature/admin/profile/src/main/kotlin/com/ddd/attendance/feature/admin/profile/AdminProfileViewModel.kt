package com.ddd.attendance.feature.admin.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.feature.core.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProfileViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        AdminProfileUiState (
            name = "김디디님",
            position = "Designer",
            team = "iOS 2팀",
            generation = "11기",
            task = "팀매니징, 일정 리마인드, 사진 촬영, 장소 대관, SNS 관리, 출석 체크",
            organization = "Dynamic Developer Designers",
            appInfo = AppInfo(
                version = BuildConfig.APP_VERSION,
                privacyPolicyUrl = "https://ddd.ac.kr/privacy",
                privacyPolicyText = "개인정보처리방침 보기"
            ),
            isShowContributorBottomSheet = false
        )
    )

    val uiState: StateFlow<AdminProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onIntent(intent: AdminProfileIntent) {
        when(intent) {
            is AdminProfileIntent.PopBackStack -> popBackStack()
            else -> {
                _uiState.update {
                    reduce(it, intent)
                }
            }
        }
    }

    private fun reduce(
        state: AdminProfileUiState,
        intent: AdminProfileIntent
    ): AdminProfileUiState {
        return when (intent) {
            is AdminProfileIntent.ShowContributorBottomSheet -> state.copy(isShowContributorBottomSheet = true)
            is AdminProfileIntent.HideContributorBottomSheet -> state.copy(isShowContributorBottomSheet = false)
            else -> state
        }
    }

    private fun popBackStack() {
        viewModelScope.launch { _navigationEvent.emit(ProfileNavigationEvent.PopBackStack) }
    }
}