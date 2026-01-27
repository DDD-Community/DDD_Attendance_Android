package com.ddd.attendance.data.google.datasource

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLoginDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GoogleLoginDataSource {
    private val credentialManager = CredentialManager.create(context)

    override suspend fun logout(): Result<Unit> {
        return try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}