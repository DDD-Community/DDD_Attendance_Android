package com.ddd.attendance.feature.member.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemberAttendanceUiState(
    val qrCodeBase64: String,
    val title: String,
    val subtitle: String,
    val isLoading: Boolean
)

@HiltViewModel
class MemberAttendanceViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MemberAttendanceUiState(
            qrCodeBase64 = "",
            title = "QR 코드를 스캔해 주세요.",
            subtitle = "스마트폰으로 QR 코드를 인식하면 출석 됩니다.",
            isLoading = true
        )
    )

    val uiState: StateFlow<MemberAttendanceUiState> = _uiState.asStateFlow()

    init {
        loadQrCode()
    }

    private fun loadQrCode() {
        viewModelScope.launch {
            // userId를 DataStore에서 가져오기
            userRepository.getUserId().collect { userId ->
                if (userId > 0) {
                    // QR 데이터 가져오기
                    userRepository.getQr(userId)

                    // 저장된 QR Base64 읽기
                    userRepository.getQrBase64().collect { qrBase64 ->
                        _uiState.value = _uiState.value.copy(
                            qrCodeBase64 = qrBase64,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}