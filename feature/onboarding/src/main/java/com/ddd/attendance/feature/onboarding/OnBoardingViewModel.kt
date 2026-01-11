package com.ddd.attendance.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.ItemSelect
import com.ddd.attendance.domain.repository.OnboardingRepository
import com.ddd.attendance.feature.core.model.UserType
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import com.ddd.attendance.feature.onboarding.select.SelectItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
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
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onIntent(intent: OnBoardingIntent) {
        when (intent) {
            is OnBoardingIntent.GoToNextStep -> handleNextStep()
            is OnBoardingIntent.GoToPreviousStep -> handlePreviousStep()
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
                if(intent.status == PinCodeStatus.Success) {
                    val type = if(intent.data?.type == "MEMBER") UserType.Member else UserType.Admin

                    val newState = state.copy(
                        type = type,
                        pinCodeStatus = PinCodeStatus.Success,
                        generationId = intent.data?.generationId ?: -1,
                        generationName = intent.data?.generationName ?: ""
                    )

                    fetchSelectList(
                        type = type,
                        generationId = newState.generationId
                    )

                    moveToStep(newState, state.index + 1)
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
                state.copy(
                    selectedItemMap =
                        state.selectedItemMap.mapValues { (step, items) ->
                            if (step == state.step) {
                                items.mapIndexed { index, item ->
                                    item.copy(
                                        isSelected = index == intent.position
                                    )
                                }.toPersistentList()
                            } else items
                        }
                )
            }
        }
    }

    private fun handleNextStep() {
        val state = _uiState.value

        when {
            state.step == OnBoardingStep.Invite && state.pinCodeStatus == PinCodeStatus.Ready -> {
                verifyPinCode(state)
            }

            state.index == MAX_STEP_INDEX -> {
                goToHome()
            }

            else -> {
                _uiState.update { reduce(it, OnBoardingIntent.GoToNextStep) }
            }
        }
    }

    private fun handlePreviousStep() {
        val state = _uiState.value

        if (state.step == OnBoardingStep.Invite) {
            popBackStack()
        } else {
            _uiState.update { reduce(it, OnBoardingIntent.GoToPreviousStep) }
        }
    }

    private fun resolveStep(
        type: UserType,
        index: Int
    ): OnBoardingStep {
        val steps = stepOrder(type)
        return steps[index.coerceIn(steps.indices)]
    }

    private fun stepOrder(type: UserType): List<OnBoardingStep> =
        when (type) {
            UserType.Member -> listOf(
                OnBoardingStep.Invite,
                OnBoardingStep.Name,
                OnBoardingStep.Job,
                OnBoardingStep.Team
            )
            UserType.Admin -> listOf(
                OnBoardingStep.Invite,
                OnBoardingStep.Name,
                OnBoardingStep.Job,
                OnBoardingStep.Role
            )
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

    private fun fetchSelectList(type: UserType, generationId: Int) {
        viewModelScope.launch {
            val flow =
                if(type == UserType.Member) onboardingRepository.getMemberSelectList(generationId)
                else onboardingRepository.getAdminSelectList()

            flow.collect { map ->
                _uiState.update { it.copy(selectedItemMap = map.toStepMap()) }
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
            step = resolveStep(state.type, nextIndex),
            index = nextIndex,
            grayBlockCount = nextIndex,
            blackBlockCount = MAX_STEP_INDEX - nextIndex
        )
    }

    private fun Map<String, List<ItemSelect>>.toStepMap(): Map<OnBoardingStep, ImmutableList<SelectItemUiModel>> {
        return this.mapKeys { (key, _) ->
            when (key) {
                "job" -> OnBoardingStep.Job
                "role" -> OnBoardingStep.Role
                "team" -> OnBoardingStep.Team
                else -> throw IllegalArgumentException("Unknown key: $key")
            }
        }.mapValues { (step, items) ->
            items.map { item ->
                SelectItemUiModel(
                    step = step,
                    text = item.name,
                    isSelected = false
                )
            }.toPersistentList()
        }
    }

    private companion object {
        const val MAX_STEP_INDEX = 3
        const val PIN_CODE_LENGTH = 4
    }
}