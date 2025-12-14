package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val googleLoginDataSource: GoogleLoginDataSource,
    private val apiLoginDataSource: ApiLoginDataSource
) : UserRepository {
    
    override fun login(loginType: LoginType): Flow<Boolean> = flow {
        try {
            when (loginType) {
                LoginType.GOOGLE -> {
                    val socialLoginResult = googleLoginDataSource.login()
                    if (socialLoginResult.isSuccess) {
                        val idToken = socialLoginResult.getOrThrow()
                        
                        val apiResult = apiLoginDataSource.login(idToken)
                        if (apiResult.isSuccess) {
                            emit(true)
                        } else {
                            emit(false)
                        }
                    } else {
                        emit(false)
                    }
                }
            }
        } catch (e: Exception) {
            emit(false)
        }
    }
}