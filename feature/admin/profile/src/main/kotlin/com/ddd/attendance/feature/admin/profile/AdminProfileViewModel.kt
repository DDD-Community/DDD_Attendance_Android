package com.ddd.attendance.feature.admin.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.usecase.GetAdminMeUseCase
import com.ddd.attendance.feature.core.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    private val getAdminMeUseCase: GetAdminMeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminProfileUiState())
    val uiState: StateFlow<AdminProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        getAdminMeUseCase()
            .onEach {
                _uiState.update { state ->
                    state.copy(
                        name = it.name,
                        jobRole = it.jobRole,
                        team = it.team,
                        generation = it.generation,
                        managerRoles = it.managerRoles.toPersistentList(),
                        organization = "Dynamic Developer Designers",
                        isShowContributorBottomSheet = false,
                        version = BuildConfig.APP_VERSION,
                        privacyPolicyUrl = "https://ddd.ac.kr/privacy",
                        privacyPolicyText = "개인정보처리방침 보기",
                    )
                }
            }
            .catch {

            }
            .launchIn(viewModelScope)
    }

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
            is AdminProfileIntent.ShowContributor -> state.copy(isShowContributorBottomSheet = intent.isShow)
            is AdminProfileIntent.ShowWithdrawAccount -> state.copy(isShowWithdrawAccountPopup = intent.isShow)
            is AdminProfileIntent.ShowLogout -> state.copy(isShowLogoutPopup = intent.isShow)
            else -> state
        }
    }

    private fun popBackStack() {
        viewModelScope.launch { _navigationEvent.emit(ProfileNavigationEvent.PopBackStack) }
    }
}