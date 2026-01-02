package com.ddd.attendance.feature.member.profile

import androidx.lifecycle.ViewModel
import com.ddd.attendance.feature.core.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val appInfo: AppInfo
)

@HiltViewModel
class MemberProfileViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(
        MemberProfileUiState(
            name = "김디디님",
            position = "Designer",
            team = "iOS 2팀",
            generation = "11기",
            organization = "Dynamic Developer Designers",
            appInfo = AppInfo(
                version = BuildConfig.APP_VERSION,
                privacyPolicyUrl = "https://ddd.ac.kr/privacy",
                privacyPolicyText = "개인정보처리방침 보기"
            )
        )
    )
    
    val uiState: StateFlow<MemberProfileUiState> = _uiState.asStateFlow()
}