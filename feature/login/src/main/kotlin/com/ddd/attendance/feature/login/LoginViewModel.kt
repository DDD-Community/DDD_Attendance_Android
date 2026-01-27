package com.ddd.attendance.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.usecase.GetUserNavigationDestinationUseCase
import com.ddd.attendance.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getUserNavigationDestinationUseCase: GetUserNavigationDestinationUseCase
) : ViewModel() {

    private val _navigationDestination = MutableSharedFlow<NavigationDestination>()
    val navigationDestination: SharedFlow<NavigationDestination> = _navigationDestination.asSharedFlow()

    fun loginWithToken(idToken: String) {
        loginUseCase(idToken = idToken, isAutoLogin = false)
            .onEach {
                checkAndNavigateToMember(it.statusCode)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun checkAndNavigateToMember(code: Int) {
        val destination = getUserNavigationDestinationUseCase(code)
        _navigationDestination.emit(destination)
    }
}