package com.ddd.attendance.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.domain.usecase.GetUserNavigationDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserNavigationDestinationUseCase: GetUserNavigationDestinationUseCase
) : ViewModel() {
    
    private val _navigationDestination = MutableSharedFlow<NavigationDestination>()
    val navigationDestination: SharedFlow<NavigationDestination> = _navigationDestination.asSharedFlow()
    
    init {
        startSplash()
    }
    
    private fun startSplash() {
        viewModelScope.launch {
            delay(2000) // 2초 후 적절한 화면으로 이동
            val destination = getUserNavigationDestinationUseCase()
            _navigationDestination.emit(destination)
        }
    }
}