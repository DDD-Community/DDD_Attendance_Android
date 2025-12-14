package com.ddd.attendance.data.google.datasource

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.ddd.attendance.data.datasource.LoginDataSource
import com.ddd.attendance.domain.model.LoginType
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLoginDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LoginDataSource {
    private val credentialManager = CredentialManager.create(context)
    
    override suspend fun login(loginType: LoginType): Result<String> {
        return when (loginType) {
            LoginType.GOOGLE -> signInWithGoogle()
        }
    }
    
    private suspend fun signInWithGoogle(): Result<String> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(SERVER_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .build()
                
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
                
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        Result.success(googleIdTokenCredential.idToken)
                    } else {
                        Result.failure(IllegalStateException("Unexpected credential type"))
                    }
                }
                else -> {
                    Result.failure(IllegalStateException("Unexpected credential type"))
                }
            }
        } catch (e: GetCredentialException) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            credentialManager.clearCredentialState(
                androidx.credentials.ClearCredentialStateRequest()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        private const val SERVER_CLIENT_ID = "369957721624-834shassrfsjt9j97oe2801clnngqtls.apps.googleusercontent.com"
    }
}