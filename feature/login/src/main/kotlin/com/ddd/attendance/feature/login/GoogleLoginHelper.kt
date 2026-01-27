package com.ddd.attendance.feature.login

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleLoginHelper(private val activity: Activity) {
    private val credentialManager = CredentialManager.create(activity)

    suspend fun signIn(): Result<String> {
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
                context = activity
            )

            when (val credential = result.credential) {
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
        } catch (e: GetCredentialException) {
            Result.failure(e)
        }
    }

    companion object {
        private const val SERVER_CLIENT_ID =
            "369957721624-834shassrfsjt9j97oe2801clnngqtls.apps.googleusercontent.com"
    }
}

@Composable
fun rememberGoogleLoginHelper(): GoogleLoginHelper {
    val context = LocalContext.current
    return remember {
        GoogleLoginHelper(context as Activity)
    }
}
