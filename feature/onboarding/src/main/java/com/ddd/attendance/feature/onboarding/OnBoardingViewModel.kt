package com.ddd.attendance.feature.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.domain.model.onboarding.ItemSelect
import com.ddd.attendance.domain.usecase.GetAdminSelectListUseCase
import com.ddd.attendance.domain.usecase.GetMemberSelectListUseCase
import com.ddd.attendance.domain.usecase.LoginUseCase
import com.ddd.attendance.domain.usecase.UsersSaveUseCase
import com.ddd.attendance.domain.usecase.VerifyCodeUseCase
import com.ddd.attendance.feature.core.model.UserType
import com.ddd.attendance.feature.onboarding.invite.PinCodeStatus
import com.ddd.attendance.feature.onboarding.select.SelectItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val getAdminSelectListUseCase: GetAdminSelectListUseCase,
    private val getMemberSelectListUseCase: GetMemberSelectListUseCase,
    private val usersSaveUseCase: UsersSaveUseCase,
    private val loginUseCase: LoginUseCase,
    private val userPreferencesDataStore: UserPreferencesDataStore
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
                moveToStep(
                    state = state,
                    nextIndex = (state.index - 1).coerceAtLeast(0)
                )
            }

            is OnBoardingIntent.GoToNextStep -> {
                moveToStep(
                    state = state,
                    nextIndex = (state.index + 1).coerceAtMost(MAX_STEP_INDEX)
                )
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
                        if (intent.pinCode.length == PIN_CODE_LENGTH)
                            PinCodeStatus.Ready
                        else PinCodeStatus.Idle
                )
            }

            is OnBoardingIntent.NameChanged -> {
                state.copy(name = intent.name)
            }

            is OnBoardingIntent.SelectListItem -> {
                //단일 선택시
                val singleMap =
                    state.selectedItemMap.mapValues { (step, items) ->
                        if (step == state.step) {
                            items.mapIndexed { index, item ->
                                item.copy(isSelected = index == intent.position)
                            }.toPersistentList()
                        } else items
                    }

                // 다중 선택 시
                val multiMap =
                    state.selectedItemMap.mapValues { (step, items) ->
                        if (step == state.step) {
                            items.mapIndexed { index, item ->
                                if (index == intent.position)
                                    item.copy(isSelected = !item.isSelected)
                                else item
                            }.toPersistentList()
                        } else items
                    }

                //단일 선택시 job
                val updatedJob =
                    if (state.step == OnBoardingStep.Job) {
                        singleMap[OnBoardingStep.Job]?.getOrNull(intent.position)?.key.toString()
                    } else state.jobRole

                //단일 선택시 teamId
                val updatedTeamId =
                    if (state.step == OnBoardingStep.Team) {
                        singleMap[OnBoardingStep.Team]?.getOrNull(intent.position)?.teamId?: 0
                    } else state.teamId

                //다중 선택시 role item
                val updatedRoleItem =
                    if (state.step == OnBoardingStep.Role) {
                        multiMap[OnBoardingStep.Role]?.getOrNull(intent.position)?.key.toString()
                    } else ""

                val updatedManagerRoles =
                    state.managerRoles
                        .toPersistentList()
                        .let {
                            if (it.contains(updatedRoleItem))
                                it.remove(updatedRoleItem)
                            else
                                it.add(updatedRoleItem)
                        }
                        .filter { it.isNotBlank() }
                        .toPersistentList()

                val map = if (state.step == OnBoardingStep.Role) multiMap else singleMap

                state.copy(
                    selectedItemMap = map,
                    jobRole = updatedJob,
                    teamId = updatedTeamId,
                    managerRoles = updatedManagerRoles
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
                submitOnboarding()
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
            verifyCodeUseCase(state.inputInvitePinCode)
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
                if(type == UserType.Member) getMemberSelectListUseCase(generationId) else getAdminSelectListUseCase()

            flow.collect { map ->
                _uiState.update { it.copy(selectedItemMap = map.toStepMap()) }
            }
        }
    }

    fun submitOnboarding() {
        goToHome()
        /*val state = _uiState.value
        submitOnboardingFlow(state)
            .flatMapConcat { loginUseCase(LoginType.GOOGLE) }
            .onEach { goToHome() }
            .catch { e -> _navigationEvent.emit(NavigationEvent.FailOnBoarding(e.message.orEmpty())) }
            .launchIn(viewModelScope)*/
    }

    private fun submitOnboardingFlow(
        state: OnBoardingUiState
    ): Flow<Unit> = flow {
        val token = userPreferencesDataStore.tempOauthToken.first().orEmpty()
        val provider = userPreferencesDataStore.tempOauthProvider.first().orEmpty()

        Log.d(
            "submitOnboardingFlow",
            """
                name = ${state.name}
                generationId = ${state.generationId}
                jobRole = ${state.jobRole}
                teamId = ${state.teamId}
                managerRoles = ${state.managerRoles}
                provider = $provider
                token = $token
                invitationCode = ${state.inputInvitePinCode}
                """.trimIndent()
        )

        //TODO: users/api 호출
        usersSaveUseCase(
            name = state.name,
            generationId = state.generationId,
            jobRole = state.jobRole,
            teamId = state.teamId,
            managerRoles = state.managerRoles,
            provider = provider,
            token = token,
            invitationCode = state.inputInvitePinCode
        ).collect {
            emit(Unit)
        }
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
                    teamId = item.teamId,
                    key = item.key,
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