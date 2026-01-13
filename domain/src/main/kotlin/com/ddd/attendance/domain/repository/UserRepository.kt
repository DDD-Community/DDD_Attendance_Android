package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.model.users.Users
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(loginType: LoginType): Flow<Unit>
    fun usersSave(
        name: String,
        generationId: Int,
        jobRole: String,
        teamId: Int,
        managerRoles: List<String>,
        provider: String,
        token: String,
        invitationCode: String
    ): Flow<Users>
    suspend fun completeOnboardingAndLogin(): Result<Unit>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getUserRole(): String?
    suspend fun hasTempOAuthToken(): Boolean

}