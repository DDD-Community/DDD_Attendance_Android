package com.ddd.attendance.feature.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.model.UsersException
import com.ddd.attendance.domain.repository.UserRepository
import com.ddd.attendance.domain.usecase.GetUserNavigationDestinationUseCase
import com.ddd.attendance.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserNavigationDestinationUseCase: GetUserNavigationDestinationUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    
    private val _navigationDestination = MutableSharedFlow<NavigationDestination>()
    val navigationDestination: SharedFlow<NavigationDestination> = _navigationDestination.asSharedFlow()
    
    /**
     * 권한 처리 후 스플래시 시작
     */
    fun startSplashAfterPermission() {
        viewModelScope.launch {
            delay(2000) // 2초 후 적절한 화면으로 이동

            loginUseCase(
                idToken = "",
                isAutoLogin = true
            ).onEach {
                val destination = getUserNavigationDestinationUseCase(code = it.statusCode)
                _navigationDestination.emit(destination)
            }.catch {
                when(it) {
                    is UsersException -> {
                        Log.e("startSplashAfterPermission", "UsersException: ${it.errorMessage}")
                        _navigationDestination.emit(NavigationDestination.Login)
                    }
                }
            }.collect()
        }
    }
}