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
import com.ddd.attendance.domain.model.UsersException
import com.ddd.attendance.domain.model.users.Users
import com.ddd.attendance.domain.model.users.UsersMe
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

    override fun users(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Flow<Users>  = flow {
        apiUsersDataSource.users(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            teamId = teamId,
            managerRoles = managerRoles,
            provider = provider,
            token = token,
            invitationCode = invitationCode
        ).onSuccess {
            emit(it.toDomain())
        }.onFailure {
            throw it
        }
    }

    override fun usersMe(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        invitationCode: String
    ): Flow<UsersMe> = flow {
        apiUsersDataSource.usersMe(
            name = name,
            generationId = generationId,
            jobRole = jobRole,
            teamId = teamId,
            managerRoles = managerRoles,
            invitationCode = invitationCode
        ).onSuccess {
            emit(it.toDomain())
        }.onFailure {
            throw it
        }
    }

    override fun login(isAutoLogin: Boolean): Flow<Login> = flow {
        val idToken =
            if (!isAutoLogin) {
                googleLoginDataSource.login().getOrNull()?.also {
                    userPreferencesDataStore.saveTempOAuthToken(it, "GOOGLE")
                }
            } else {
                userPreferencesDataStore.tempOauthToken.firstOrNull()
            }

        if (idToken == null) {
            throw UsersException.Unknown(code = 402, errorMessage = "Unknown OauthToken")
        }

        apiLoginDataSource.login(idToken)
            .onSuccess { codeResult ->
                val login =
                    codeResult.response.body()?.toDomain(statusCode = codeResult.code)

                login?.let {
                    userPreferencesDataStore.saveUserRole(it.role)
                    emit(it)
                }?: run {
                    throw UsersException.BadRequest(errorMessage = "Login response body is null")
                }
            }
            .onFailure { throw it }
    }

    override fun completeOnboardingAndLogin(): Flow<Login> = flow {
        // 토큰 없으면 emit 없이 종료
        val tempToken = userPreferencesDataStore.tempOauthToken.firstOrNull() ?: return@flow

        apiLoginDataSource.login(tempToken)
            .onSuccess { codeResult ->
                val login = codeResult.response.body()?.toDomain(statusCode = codeResult.code)

                login?.let {
                    userPreferencesDataStore.saveUserRole(it.role)
                    emit(it)
                }
            }.onFailure {
                throw it
            }
    }

    override fun logout(): Flow<Unit> = flow {
        apiLoginDataSource.logout()

        emit(Unit)
    }

    override fun deleteUsersMe(): Flow<Unit> = flow {
        val token = userPreferencesDataStore.tempOauthToken.firstOrNull() ?: throw IllegalStateException("No temp OAuth token found")

        apiUsersDataSource.deleteUsersMe(token)

        emit(Unit)
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

    override fun getSchedules(): Flow<List<Schedule>> = flow {
        apiMeDataSource.getSchedules()
            .onSuccess {
                emit(
                    it.map { data ->
                        data.toDomain()
                    }
                )
            }
            .onFailure {
                throw it
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