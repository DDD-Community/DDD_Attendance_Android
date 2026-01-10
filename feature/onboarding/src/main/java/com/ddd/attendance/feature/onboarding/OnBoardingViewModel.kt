package com.ddd.attendance.feature.onboarding

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.repository.OnboardingRepository
import com.ddd.attendance.feature.core.model.UserType
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onIntent(intent: OnBoardingIntent) {
        when (intent) {
            is OnBoardingIntent.GoToNextStep -> {
                val current = _uiState.value

                if (current.step == OnBoardingStep.Invite && current.pinCodeStatus == PinCodeStatus.Ready) {
                    verifyPinCode(current)
                } else if (current.index == MAX_STEP_INDEX) {
                    goToHome()
                } else _uiState.update { reduce(it, intent) }
            }

            is OnBoardingIntent.GoToPreviousStep -> {
                val current = _uiState.value

                if (current.step == OnBoardingStep.Invite) {
                    popBackStack()
                } else _uiState.update { reduce(it, intent) }

            }

            else -> _uiState.update { reduce(it, intent) }
        }
    }

    private fun reduce(
        state: OnBoardingUiState,
        intent: OnBoardingIntent
    ): OnBoardingUiState {
        return when (intent) {
            is OnBoardingIntent.GoToPreviousStep -> {
                val prev = (state.index - 1).coerceAtLeast(0)
                moveToStep(state, prev)
            }

            is OnBoardingIntent.GoToNextStep -> {
                val next = (state.index + 1).coerceAtMost(MAX_STEP_INDEX)
                moveToStep(state, next)
            }

            is OnBoardingIntent.VerifyPinCodeResult -> {
                if (intent.status == PinCodeStatus.Success) {
                    moveToStep(
                        state = state.copy(
                            pinCodeStatus = PinCodeStatus.Success,
                            type = if (intent.data?.type == "MEMBER") UserType.Member else UserType.Admin,
                            generationId = intent.data?.generationId?: -1,
                            generationName = intent.data?.generationName?: ""

                        ),
                        nextIndex = state.index + 1
                    )
                } else state.copy(pinCodeStatus = PinCodeStatus.Fail)
            }

            is OnBoardingIntent.InvitePinCodeChanged -> {
                state.copy(
                    inputInvitePinCode = intent.pinCode,
                    pinCodeStatus =
                        if (intent.pinCode.length == PIN_CODE_LENGTH) {
                            PinCodeStatus.Ready
                        } else PinCodeStatus.Idle
                )
            }

            is OnBoardingIntent.NameChanged -> state.copy(name = intent.name)

            is OnBoardingIntent.SelectListItem -> {
                val updatedMap =
                    state.dummyList.mapValues { (step, items) ->
                        if (step == state.step) {
                            items
                                .mapIndexed { index, item ->
                                    item.copy(isSelected = index == intent.position)
                                }
                                .toPersistentList()
                        } else {
                            items
                        }
                    }

                state.copy(dummyList = updatedMap)
            }
        }
    }

    private fun resolveStep(type: UserType, index: Int): OnBoardingStep {
        val steps = when (type) {
            UserType.Admin -> listOf(
                OnBoardingStep.Invite,
                OnBoardingStep.Name,
                OnBoardingStep.Role,
                OnBoardingStep.Work
            )
            UserType.Member -> listOf(
                OnBoardingStep.Invite,
                OnBoardingStep.Name,
                OnBoardingStep.Role,
                OnBoardingStep.Team
            )
        }
        return steps[index.coerceIn(0, steps.lastIndex)]
    }

    private fun verifyPinCode(state: OnBoardingUiState) {
        viewModelScope.launch {
            onboardingRepository
                .verifyCode(state.inputInvitePinCode)
                .catch {
                    onIntent(
                        OnBoardingIntent.VerifyPinCodeResult(
                            status = PinCodeStatus.Fail
                        )
                    )
                }
                .collect {
                    onIntent(
                        OnBoardingIntent.VerifyPinCodeResult(
                            status = PinCodeStatus.Success,
                            data = it,
                        )
                    )
                }
        }
    }

    private fun popBackStack() {
        viewModelScope.launch { _navigationEvent.emit(NavigationEvent.PopBackStack) }
    }

    private fun goToHome() {
        viewModelScope.launch { _navigationEvent.emit(NavigationEvent.GoToHome) }
    }

    private fun moveToStep(state: OnBoardingUiState, nextIndex: Int): OnBoardingUiState {
        return state.copy(
            step = resolveStep(state.dummyType, nextIndex),
            index = nextIndex,
            grayBlockCount = nextIndex,
            blackBlockCount = MAX_STEP_INDEX - nextIndex
        )
    }

    private companion object {
        const val MAX_STEP_INDEX = 3
        const val PIN_CODE_LENGTH = 4
    }
}
