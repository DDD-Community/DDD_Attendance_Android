package com.ddd.attendance.feature.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState.asStateFlow()

    //dummy data
    val dummyType = OnBoardingType.Member

    fun onIntent(intent: OnBoardingIntent) {
        when (intent) {
            is OnBoardingIntent.NextStepBlock -> {
                val current = _uiState.value

                // PIN 코드 검증이 필요한 상황
                if (current.step == OnBoardingStep.Invite && current.pinCodeStatus == PinCodeStatus.Ready) {

                    viewModelScope.launch {
                        delay(1000L)
                        // 여기서 성공/실패를 보내는 것으로 분리
                        // 테스트할 때는 이 부분만 Mock
                        val result = if (current.invitePinCode == "1234") {
                            PinCodeStatus.Success
                        } else PinCodeStatus.Fail

                        onIntent(OnBoardingIntent.VerifyPinCodeResult(result))
                    }
                } else {
                    _uiState.update { reduce(it, intent) }
                }
            }

            else -> _uiState.update { reduce(it, intent) }
        }
    }

    private fun reduce(
        state: OnBoardingUiState,
        intent: OnBoardingIntent
    ): OnBoardingUiState {

        return when (intent) {

            is OnBoardingIntent.BackStepBlock -> {
                val next = (state.index - 1).coerceAtLeast(0)
                state.copy(
                    step = resolveStep(dummyType, next),
                    index = next,
                    grayBlockCount = 3 - next,
                    blackBlockCount = next
                )
            }

            is OnBoardingIntent.NextStepBlock -> {
                // 일반 NEXT 처리
                val next = (state.index + 1).coerceAtMost(3)
                state.copy(
                    step = resolveStep(dummyType, next),
                    index = next,
                    grayBlockCount = 3 - next,
                    blackBlockCount = next
                )
            }

            is OnBoardingIntent.VerifyPinCodeResult -> {
                when (intent.result) {
                    PinCodeStatus.Success -> {
                        // 성공 시 다음 단계로 이동
                        val next = (state.index + 1).coerceAtMost(3)
                        state.copy(
                            pinCodeStatus = PinCodeStatus.Success,
                            step = resolveStep(dummyType, next),
                            index = next,
                            grayBlockCount = 3 - next,
                            blackBlockCount = next
                        )
                    }

                    PinCodeStatus.Fail -> {
                        state.copy(pinCodeStatus = PinCodeStatus.Fail)
                    }

                    else -> state
                }
            }

            is OnBoardingIntent.InvitePinCodeChanged -> {
                val newStatus =
                    if (intent.pinCode.length == 4) PinCodeStatus.Ready else PinCodeStatus.Idle

                state.copy(
                    invitePinCode = intent.pinCode,
                    pinCodeStatus = newStatus
                )
            }
        }
    }

    private fun resolveStep(type: OnBoardingType, index: Int): OnBoardingStep {
        val steps = when (type) {
            OnBoardingType.Admin -> listOf(
                OnBoardingStep.Invite,
                OnBoardingStep.Name,
                OnBoardingStep.Role,
                OnBoardingStep.Work
            )
            OnBoardingType.Member -> listOf(
                OnBoardingStep.Invite,
                OnBoardingStep.Name,
                OnBoardingStep.Role,
                OnBoardingStep.Team
            )
        }
        return steps[index.coerceIn(0, steps.lastIndex)]
    }
}
