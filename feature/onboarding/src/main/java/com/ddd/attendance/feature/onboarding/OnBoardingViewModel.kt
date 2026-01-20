package com.ddd.attendance.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.domain.model.onboarding.ItemSelect
import com.ddd.attendance.domain.model.onboarding.OnboardingEntryPoint
import com.ddd.attendance.domain.usecase.CompleteOnboardingAndLoginUseCase
import com.ddd.attendance.domain.usecase.GetAdminSelectListUseCase
import com.ddd.attendance.domain.usecase.GetMemberSelectListUseCase
import com.ddd.attendance.domain.usecase.GetUserNavigationDestinationUseCase
import com.ddd.attendance.domain.usecase.UsersMeUseCase
import com.ddd.attendance.domain.usecase.UsersUseCase
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val getAdminSelectListUseCase: GetAdminSelectListUseCase,
    private val getMemberSelectListUseCase: GetMemberSelectListUseCase,
    private val usersUseCase: UsersUseCase,
    private val usersMeUseCase: UsersMeUseCase,
    private val completeOnboardingAndLoginUseCase: CompleteOnboardingAndLoginUseCase,
    private val getUserNavigationDestinationUseCase: GetUserNavigationDestinationUseCase,
    private val userPreferencesDataStore: UserPreferencesDataStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<OnboardingNavigationEvent>()
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
            // 온보딩 화면 초기화, entryPoint 설정 (회원가입 / 프로필 수정 구분)
            is OnBoardingIntent.Initialize -> {
                state.copy(entryPoint = intent.entryPoint)
            }

            // 이전 단계로 이동 요청
            is OnBoardingIntent.GoToPreviousStep -> {
                moveToStep(
                    state = state,
                    nextIndex = (state.index - 1).coerceAtLeast(0)
                )
            }

            // 다음 단계로 이동 요청
            is OnBoardingIntent.GoToNextStep -> {
                moveToStep(
                    state = state,
                    nextIndex = (state.index + 1).coerceAtMost(state.maxStepIndex)
                )
            }

            // 핀코드 검증 결과 처리
            // 성공이면 유저 타입, maxStepIndex, generationId 등 설정 후 다음 단계 이동
            is OnBoardingIntent.VerifyPinCodeResult -> {
                val isMember = intent.data?.type == "MEMBER"

                if (intent.status == PinCodeStatus.Success) {
                    // 유저 타입 결정
                    val type = if (isMember) UserType.MEMBER else UserType.ADMIN

                    // 단계 리스트 결정
                    val steps = when (type) {
                        UserType.MEMBER -> listOf(
                            OnBoardingStep.Invite,
                            OnBoardingStep.Name,
                            OnBoardingStep.Job,
                            OnBoardingStep.Team
                        )
                        else -> listOf(
                            OnBoardingStep.Invite,
                            OnBoardingStep.Name,
                            OnBoardingStep.Job,
                            OnBoardingStep.Role,
                            OnBoardingStep.Team
                        )
                    }.toPersistentList()

                    // 최대 단계 인덱스
                    val maxStepIndex = if (isMember) MAX_STEP_INDEX else steps.lastIndex

                    val newState = state.copy(
                        type = type,
                        stepItems = steps,
                        maxStepIndex = maxStepIndex,
                        pinCodeStatus = PinCodeStatus.Success,
                        generationId = intent.data?.generationId ?: -1,
                        generationName = intent.data?.generationName ?: ""
                    )

                    // 유저 타입에 맞는 api 호출
                    fetchSelectList(type, newState.generationId)

                    moveToStep(newState, state.index + 1)
                } else {
                    state.copy(pinCodeStatus = PinCodeStatus.Fail)
                }
            }

            // 초대 핀코드 입력 값이 변경될 때 상태 업데이트
            // 길이가 맞으면 Ready, 아니면 Idle
            is OnBoardingIntent.InvitePinCodeChanged -> {
                state.copy(
                    inputInvitePinCode = intent.pinCode,
                    pinCodeStatus =
                        if (intent.pinCode.length == PIN_CODE_LENGTH) PinCodeStatus.Ready
                        else PinCodeStatus.Idle
                )
            }

            // 이름 입력 변경
            is OnBoardingIntent.NameChanged -> {
                state.copy(name = intent.name)
            }

            // 선택 리스트에서 아이템 선택
            // 단일 선택(Job/Team)과 다중 선택(Role)을 구분해서 상태 업데이트
            is OnBoardingIntent.SelectListItem -> {
                // 단일 선택 시 처리
                val singleMap =
                    state.selectedItemMap.mapValues { (step, items) ->
                        if (step == state.step) {
                            items.mapIndexed { index, item ->
                                item.copy(isSelected = index == intent.position)
                            }.toPersistentList()
                        } else items
                    }

                // 다중 선택 시 처리
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

                // 단일 선택 시 Job 값 업데이트
                val updatedJob =
                    if (state.step == OnBoardingStep.Job) {
                        singleMap[OnBoardingStep.Job]?.getOrNull(intent.position)?.key.toString()
                    } else state.jobRole

                // 단일 선택 시 Team ID 값 업데이트
                val updatedTeamId =
                    if (state.step == OnBoardingStep.Team) {
                        singleMap[OnBoardingStep.Team]?.getOrNull(intent.position)?.teamId?: 0
                    } else state.teamId

                // 다중 선택 시 Role 값 업데이트
                val updatedRoleItem =
                    if (state.step == OnBoardingStep.Role) {
                        multiMap[OnBoardingStep.Role]?.getOrNull(intent.position)?.key.toString()
                    } else ""

                // Manager Roles 상태 업데이트
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

                // Role 단계에서 "팀매니징" 선택 여부 확인
                val isTeamManagingSelected =
                    if (state.step == OnBoardingStep.Role) {
                        multiMap[OnBoardingStep.Role]?.any { it.isSelected && it.text == "팀매니징" } ?: false
                    } else false

                val map = if (state.step == OnBoardingStep.Role) multiMap else singleMap

                state.copy(
                    selectedItemMap = map,
                    jobRole = updatedJob,
                    teamId = updatedTeamId,
                    managerRoles = updatedManagerRoles,
                    isTeamManagingSelected = isTeamManagingSelected
                )
            }
        }
    }

    private fun handleNextStep() {
        val state = _uiState.value

        when {
            //초대 코드 입력 단계이며, 검증 가능한 상태일 때
            state.step == OnBoardingStep.Invite && state.pinCodeStatus == PinCodeStatus.Ready -> {
                verifyPinCode(state)
            }
            else -> {
                if (state.type != UserType.MEMBER) {
                    // 운영진인 경우
                    if (state.isTeamManagingSelected) {
                        // 팀 매니징 역할을 선택한 경우
                        _uiState.update { reduce(it, OnBoardingIntent.GoToNextStep) }
                    } else if (state.index == state.maxStepIndex) {
                        submitOnboarding()
                    } else {
                        if (state.index == MAX_STEP_INDEX) submitOnboarding()
                        else _uiState.update { reduce(it, OnBoardingIntent.GoToNextStep) }
                    }

                } else {
                    // 멤버인 경우
                    if (state.index == state.maxStepIndex) submitOnboarding()
                    else _uiState.update { reduce(it, OnBoardingIntent.GoToNextStep) }
                }
            }
        }
    }

    private fun handlePreviousStep() {
        val state = _uiState.value

        if (state.step == OnBoardingStep.Invite) popBackStack() // 첫 단계(초대 코드 입력)인 경우 이전 화면으로 돌아감
        else _uiState.update { reduce(it, OnBoardingIntent.GoToPreviousStep) } // 그 외 일반적인 경우, 이전 온보딩 단계로 이동
    }

    private fun popBackStack() {
        viewModelScope.launch { _navigationEvent.emit(OnboardingNavigationEvent.PopBackStack) }
    }

    private fun goToHome(statusCode: Int) {
        viewModelScope.launch {
            val destination = getUserNavigationDestinationUseCase(statusCode)

            val route = when (destination) {
                is com.ddd.attendance.domain.model.NavigationDestination.Member -> "MEMBER_MAIN"
                is com.ddd.attendance.domain.model.NavigationDestination.Manager -> "ADMIN_MAIN"
                else -> ""
            }
            _navigationEvent.emit(OnboardingNavigationEvent.GoToDestination(route))
        }
    }

    private fun moveToStep(state: OnBoardingUiState, nextIndex: Int): OnBoardingUiState {
        // 최대 값 지정
        val maxBlock = nextIndex.coerceAtMost(3)

        return state.copy(
            step = state.stepItems[nextIndex],
            index = nextIndex,
            grayBlockCount = maxBlock,
            blackBlockCount = MAX_STEP_INDEX - nextIndex
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
            val flow = if(type == UserType.MEMBER) getMemberSelectListUseCase(generationId) else getAdminSelectListUseCase(generationId)

            flow.collect { map ->
                _uiState.update { it.copy(selectedItemMap = map.toStepMap()) }
            }
        }
    }

    fun submitOnboarding() {
        val state = _uiState.value

        when (state.entryPoint) {
            OnboardingEntryPoint.SIGN_UP -> {
                onboardingPipeline(state)
                    .flatMapConcat { completeOnboardingAndLoginUseCase() }
                    .onEach { login ->
                        goToHome(statusCode = login.statusCode)
                    }
                    .launchIn(viewModelScope)
            }

            OnboardingEntryPoint.PROFILE_EDIT -> {
                onboardingPipeline(state)
                    .onEach {
                        _navigationEvent.emit(OnboardingNavigationEvent.RestartApp)
                    }
                    .launchIn(viewModelScope)
            }
        }
    }

    private fun onboardingPipeline(
        state: OnBoardingUiState
    ): Flow<Unit> =
        when (state.entryPoint) {
            OnboardingEntryPoint.SIGN_UP ->
                combine(
                    userPreferencesDataStore.tempOauthToken,
                    userPreferencesDataStore.tempOauthProvider
                ) { token, provider ->
                    token.orEmpty() to provider.orEmpty()
                }.flatMapConcat { (token, provider) ->
                    usersUseCase(
                        name = state.name,
                        generationId = state.generationId,
                        jobRole = state.jobRole,
                        teamId = state.teamId,
                        managerRoles = state.managerRoles,
                        provider = provider,
                        token = token,
                        invitationCode = state.inputInvitePinCode
                    )
                        .catch {
                            /* code 400 이미 가입된 회원 정보입니다... 에러 스킵
                            이후 로그인 요청으로 정상 동작 */
                        }
                }

            OnboardingEntryPoint.PROFILE_EDIT ->
                usersMeUseCase(
                    name = state.name,
                    generationId = state.generationId,
                    jobRole = state.jobRole,
                    teamId = state.teamId,
                    managerRoles = state.managerRoles,
                    invitationCode = state.inputInvitePinCode
                ).onEach {
                    userPreferencesDataStore.saveUserRole(it.role)
                }
        }.map { Unit }


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
        const val TAG = "OnBoardingViewModel"
        const val MAX_STEP_INDEX = 3
        const val PIN_CODE_LENGTH = 4
    }
}