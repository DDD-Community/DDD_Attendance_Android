package com.ddd.attendance.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.repository.UserRepository
import com.ddd.attendance.domain.usecase.GetUserNavigationDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserNavigationDestinationUseCase: GetUserNavigationDestinationUseCase
) : ViewModel() {

    private val _navigationDestination = MutableSharedFlow<NavigationDestination>()
    val navigationDestination: SharedFlow<NavigationDestination> = _navigationDestination.asSharedFlow()

    fun login(loginType: LoginType) {
        userRepository
            .login(loginType)
            .onEach { checkAndNavigateToMember() }
            .catch { /** TODO: 에러 처리 - exception을 사용해서 에러 상태 관리 */ }
            .launchIn(viewModelScope)
    }

    private suspend fun checkAndNavigateToMember() {
        val destination = getUserNavigationDestinationUseCase()
        _navigationDestination.emit(destination)
    }
}