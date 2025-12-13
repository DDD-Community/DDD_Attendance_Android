package com.ddd.attendance.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigateToNext = MutableSharedFlow<Unit>()
    val navigateToNext: SharedFlow<Unit> = _navigateToNext.asSharedFlow()

    fun login(loginType: LoginType) {
        viewModelScope.launch {
            userRepository.login(loginType)
                .onSuccess {
                    _navigateToNext.emit(Unit)
                }
                .onFailure {
                    // TODO: 에러 처리
                }
        }
    }
}