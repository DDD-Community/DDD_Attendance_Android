package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.Login
import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.model.users.Users
import com.ddd.attendance.domain.model.users.UsersMe
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun users(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int?,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Flow<Users>

    fun usersMe(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        managerRoles: List<String>,
        invitationCode: String
    ): Flow<UsersMe>

    fun login(isAutoLogin: Boolean): Flow<Login>
    fun completeOnboardingAndLogin(): Flow<Login>
    fun logout(): Flow<Unit>
    fun deleteUsersMe(): Flow<Unit>
    fun getSchedules(): Flow<List<Schedule>>

    suspend fun isUserLoggedIn(): Boolean
    suspend fun getUserRole(): String?
    suspend fun hasTempOAuthToken(): Boolean
    suspend fun getMe(): Result<Unit>
    suspend fun getQr(userId: Long): Result<Unit>

    fun getQrBase64(): Flow<String>
    fun getUserId(): Flow<Long>
    fun getUserName(): Flow<String>
    fun getUserGeneration(): Flow<String>
    fun getUserTeam(): Flow<String>
    fun getUserJobRole(): Flow<String>

    suspend fun getAttendanceCount(): Int
    suspend fun getLateCount(): Int
    suspend fun getAbsentCount(): Int
    suspend fun fetchActivitySchedules(): Result<Unit>
    fun getActivityPeriod(): Flow<String>
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveUserRole(role: String)

    suspend fun deleteDataStoreWithdrawAccount(isLogout: Boolean)

}