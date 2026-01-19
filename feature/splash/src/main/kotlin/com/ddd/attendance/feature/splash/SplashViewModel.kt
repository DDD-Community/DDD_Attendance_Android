package com.ddd.attendance.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.usecase.DeleteDataStoreWithdrawAccountUseCase
import com.ddd.attendance.domain.usecase.GetUserNavigationDestinationUseCase
import com.ddd.attendance.domain.usecase.LoginUseCase
import com.ddd.attendance.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserNavigationDestinationUseCase: GetUserNavigationDestinationUseCase,
    private val deleteDataStoreWithdrawAccountUseCase: DeleteDataStoreWithdrawAccountUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    
    private val _navigationDestination = MutableSharedFlow<NavigationDestination>()
    val navigationDestination: SharedFlow<NavigationDestination> = _navigationDestination.asSharedFlow()
    
    /**
     * 권한 처리 후 스플래시 시작
     */
    fun startSplashAfterPermission() {
        viewModelScope.launch {
            delay(2000) // 2초 후 적절한 화면으로 이동

            val destination = getUserNavigationDestinationUseCase()

            when (destination) {
                NavigationDestination.Manager, NavigationDestination.Member -> {
                    loginUseCase(
                        loginType = LoginType.GOOGLE,
                        isAutoLogin = true
                    )
                        .onEach {

                        }
                        .catch {

                    }.collect {

                    }
                } else -> {}
            }
            _navigationDestination.emit(destination)
        }
    }
}