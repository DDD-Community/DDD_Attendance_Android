package com.ddd.attendance.domain.repository

import com.ddd.attendance.domain.model.JobRole
import com.ddd.attendance.domain.model.Team
import com.ddd.attendance.domain.model.VerifyCode
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun verifyCode(code: String): Flow<VerifyCode>
    fun getTeams(id: Int): Flow<List<Team>>
    fun getJobs(): Flow<List<JobRole>>
    fun getRole(): Flow<List<JobRole>>
}
