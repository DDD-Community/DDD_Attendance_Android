package com.ddd.attendance.feature.member.attendance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MemberAttendanceUiState(
    val qrCodeText: String,
    val title: String,
    val subtitle: String
)

@HiltViewModel
class MemberAttendanceViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(
        MemberAttendanceUiState(
            qrCodeText = "seminzzang",
            title = "QR 코드를 스캔해 주세요.",
            subtitle = "스마트폰으로 QR 코드를 인식하면 출석 됩니다."
        )
    )
    
    val uiState: StateFlow<MemberAttendanceUiState> = _uiState.asStateFlow()
}