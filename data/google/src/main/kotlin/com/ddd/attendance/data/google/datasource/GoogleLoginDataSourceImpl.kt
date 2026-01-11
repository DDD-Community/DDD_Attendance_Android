package com.ddd.attendance.data.google.datasource

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLoginDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GoogleLoginDataSource {
    private val credentialManager = CredentialManager.create(context)

    override suspend fun login(): Result<String> {
        return try {
            attemptLogin()
        } catch (e: GetCredentialCancellationException) {
            // 계정 재인증 실패 - 자격증명 캐시 초기화 후 재시도
            clearCredentialState()
            try {
                attemptLogin()
            } catch (retryException: Exception) {
                Result.failure(retryException)
            }
        } catch (e: NoCredentialException) {
            // 자격증명 없음 - 캐시 초기화 후 재시도
            clearCredentialState()
            try {
                attemptLogin()
            } catch (retryException: Exception) {
                Result.failure(retryException)
            }
        } catch (e: GetCredentialException) {
            Result.failure(e)
        }
    }

    private suspend fun attemptLogin(): Result<String> {
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(SERVER_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = context
        )

        return when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    Result.success(googleIdTokenCredential.idToken)
                } else {
                    Result.failure(IllegalStateException("Unexpected credential type"))
                }
            }

            else -> {
                Result.failure(IllegalStateException("Unexpected credential type"))
            }
        }
    }

    private suspend fun clearCredentialState() {
        try {
            credentialManager.clearCredentialState(
                androidx.credentials.ClearCredentialStateRequest()
            )
        } catch (e: Exception) {
            // 무시 - 재시도 로직이 실패해도 계속 진행
        }
    }

    override suspend fun signOut(): Result<Unit> {
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
        private const val SERVER_CLIENT_ID =
            "369957721624-834shassrfsjt9j97oe2801clnngqtls.apps.googleusercontent.com"
    }
}