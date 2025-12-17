package com.ddd.attendance.data.api.datasource

import com.ddd.attendance.data.api.UserApi
import com.ddd.attendance.data.datasource.ApiLoginDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiLoginDataSourceImpl @Inject constructor(
    private val userApi: UserApi
) : ApiLoginDataSource {
    
    override suspend fun login(idToken: String): Result<Unit> {
        return try {
            // TODO: 실제 API 호출 구현
            // userApi.login(idToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}