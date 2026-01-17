package com.ddd.attendance.feature.member.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.usecase.DeleteDataStoreWithdrawAccountUseCase
import com.ddd.attendance.domain.usecase.DeleteUsersMeUseCase
import com.ddd.attendance.domain.usecase.GetUserInfoUseCase
import com.ddd.attendance.feature.core.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppInfo(
    val version: String,
    val privacyPolicyUrl: String,
    val privacyPolicyText: String
)

data class MemberProfileUiState(
    val name: String,
    val position: String,
    val team: String,
    val generation: String,
    val organization: String,
    val appInfo: AppInfo,
    val isShowContributorBottomSheet: Boolean = false,
    val isShowWithdrawAccountPopup: Boolean = false,
    val isShowLogoutPopup: Boolean = false
)

@HiltViewModel
class MemberProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUsersMeUseCase: DeleteUsersMeUseCase,
    private val deleteDataStoreWithdrawAccountUseCase: DeleteDataStoreWithdrawAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MemberProfileUiState(
            name = "",
            position = "",
            team = "",
            generation = "",
            organization = "Dynamic Developer Designers",
            appInfo = AppInfo(
                version = BuildConfig.APP_VERSION,
                privacyPolicyUrl = "https://ddd.ac.kr/privacy",
                privacyPolicyText = "개인정보처리방침 보기"
            )
        )
    )

    val uiState: StateFlow<MemberProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserInfoUseCase().collect { userInfo ->
                _uiState.value = _uiState.value.copy(
                    name = "${userInfo.name}님",
                    position = userInfo.jobRole,
                    team = userInfo.team,
                    generation = userInfo.generation
                )
            }
        }
    }

    fun showContributorBottomSheet(isShow: Boolean) {
        _uiState.value = _uiState.value.copy(isShowContributorBottomSheet = isShow)
    }

    fun showWithdrawAccountPopup(isShow: Boolean) {
        _uiState.value = _uiState.value.copy(isShowWithdrawAccountPopup = isShow)
    }

    fun showLogoutPopup(isShow: Boolean) {
        _uiState.value = _uiState.value.copy(isShowLogoutPopup = isShow)
    }

    fun onWithdrawAccount() {
        viewModelScope.launch {
            deleteUsersMeUseCase()
                .flatMapConcat {
                    deleteDataStoreWithdrawAccountUseCase(isLogout = false)
                }
                .onCompletion { // 모든 Flow 정상 완료 시
                    _navigationEvent.emit(ProfileNavigationEvent.GoToLogin)
                }
                .catch { throwable ->

                }
                .collect {}
        }

        showWithdrawAccountPopup(false)
    }

    fun onLogout() {
        viewModelScope.launch {
            deleteDataStoreWithdrawAccountUseCase(isLogout = true)
                .onEach {
                    _navigationEvent.emit(ProfileNavigationEvent.GoToLogin)
                }
                .catch {

                }
                .collect {

                }
        }
        showLogoutPopup(false)
    }

    fun popBackStack() {
        viewModelScope.launch { _navigationEvent.emit(ProfileNavigationEvent.PopBackStack) }
    }
}