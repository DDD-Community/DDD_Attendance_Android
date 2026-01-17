package com.ddd.attendance.data.repository

import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datasource.ApiMeDataSource
import com.ddd.attendance.data.datasource.ApiSchedulesDataSource
import com.ddd.attendance.data.datasource.ApiUsersDataSource
import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.mapper.login.toDomain
import com.ddd.attendance.data.mapper.schedule.toDomain
import com.ddd.attendance.data.mapper.users.toDomain
import com.ddd.attendance.domain.model.Login
import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.model.users.Users
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val googleLoginDataSource: GoogleLoginDataSource,
    private val apiLoginDataSource: ApiLoginDataSource,
    private val apiUsersDataSource: ApiUsersDataSource,
    private val apiMeDataSource: ApiMeDataSource,
    private val apiSchedulesDataSource: ApiSchedulesDataSource,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : UserRepository {

    override fun usersSave(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Flow<Users>  = flow {
        val result = apiUsersDataSource.users(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            teamId = teamId,
            managerRoles = managerRoles,
            provider = provider,
            token = token,
            invitationCode = invitationCode
        )
        val response = result.getOrThrow()

        emit(response.toDomain())
    }

    override suspend fun login(isAutoLogin: Boolean): Result<Login> {
        return runCatching {
            //로그인 버튼
            val idToken = if (!isAutoLogin) {
                val socialLoginResult = googleLoginDataSource.login()

                if (socialLoginResult.isFailure) {
                    throw IllegalStateException("Google login failed")
                }

                val idToken = socialLoginResult.getOrThrow()

                userPreferencesDataStore.saveTempOAuthToken(token = idToken, provider = "GOOGLE")

                idToken
            } else {
                //스플래시
                userPreferencesDataStore.tempOauthToken.firstOrNull() ?: error("No temp OAuth token found")
            }

            val data = apiLoginDataSource.login(idToken).getOrThrow()
            val result = data.response.body()?.toDomain(data.code)
                ?: return Result.failure(IllegalStateException("Login response body is null"))

            userPreferencesDataStore.saveUserRole(result.role)

            result
        }
    }

    //온보딩 직후
    override suspend fun completeOnboardingAndLogin(): Result<Login> {
        return runCatching {
            val tempToken = userPreferencesDataStore.tempOauthToken.firstOrNull()
                ?: error("No temp OAuth token found")

            // 성공 시에만 임시 토큰 제거
            //userPreferencesDataStore.clearTempOAuthData()

            val data = apiLoginDataSource.login(tempToken).getOrThrow()
            val result = data.response.body()?.toDomain(data.code)
                ?: return Result.failure(IllegalStateException("Login response body is null"))

            userPreferencesDataStore.saveUserRole(result.role)

            result
        }
    }

    override suspend fun deleteUsersMe(): Result<Unit> {
        val token = userPreferencesDataStore.tempOauthToken.firstOrNull() ?: error("No temp OAuth token found")
        return apiUsersDataSource.deleteUsersMe(token).map { Unit }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val accessToken = userPreferencesDataStore.accessToken.firstOrNull()
        return !accessToken.isNullOrEmpty()
    }

    override suspend fun getUserRole(): String? {
        return userPreferencesDataStore.role.firstOrNull()
    }

    override suspend fun hasTempOAuthToken(): Boolean {
        val tempToken = userPreferencesDataStore.tempOauthToken.firstOrNull()
        return !tempToken.isNullOrEmpty()
    }

    override suspend fun getMe(): Result<Unit> {
        return apiMeDataSource.getMe()
    }

    override suspend fun getSchedules(): Result<List<Schedule>> {
        return apiMeDataSource.getSchedules().mapCatching { schedules ->
            schedules.map { it.toDomain() }
        }
    }

    override suspend fun getQr(userId: Long): Result<Unit> {
        return apiUsersDataSource.getQr(userId).map { Unit }
    }

    override fun getQrBase64(): Flow<String> {
        return userPreferencesDataStore.qrBase64.map { it ?: "" }
    }

    override fun getUserId(): Flow<Long> {
        return userPreferencesDataStore.userId.map { it ?: 0L }
    }

    override fun getUserName(): Flow<String> {
        return userPreferencesDataStore.name.map { it ?: "" }
    }

    override fun getUserGeneration(): Flow<String> {
        return userPreferencesDataStore.generation.map { it ?: "" }
    }

    override fun getUserTeam(): Flow<String> {
        return userPreferencesDataStore.team.map { it ?: "" }
    }

    override fun getUserJobRole(): Flow<String> {
        return userPreferencesDataStore.jobRole.map { it ?: "" }
    }

    override suspend fun getAttendanceCount(): Int {
        return userPreferencesDataStore.totalAttended.firstOrNull() ?: 0
    }

    override suspend fun getLateCount(): Int {
        return userPreferencesDataStore.totalLate.firstOrNull() ?: 0
    }

    override suspend fun getAbsentCount(): Int {
        return userPreferencesDataStore.totalAbsent.firstOrNull() ?: 0
    }

    override suspend fun fetchActivitySchedules(): Result<Unit> {
        return apiSchedulesDataSource.getSchedules().map { Unit }
    }

    override fun getActivityPeriod(): Flow<String> {
        return userPreferencesDataStore.activityStartDate.map { startDate ->
            val endDate = userPreferencesDataStore.activityEndDate.firstOrNull()
            if (startDate != null && endDate != null) {
                "$startDate - $endDate"
            } else {
                ""
            }
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        userPreferencesDataStore.saveAccessToken(accessToken)
    }

    override suspend fun saveUserRole(role: String) {
        userPreferencesDataStore.saveUserRole(role)
    }

    override suspend fun deleteDataStoreWithdrawAccount(isLogout: Boolean) {
        if (isLogout) {
            googleLoginDataSource.logout()
        }
        userPreferencesDataStore.clearAll()
    }
}