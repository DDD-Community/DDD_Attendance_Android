package com.ddd.attendance.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val KEY_USER_ID = longPreferencesKey("user_id")
        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_GENERATION = stringPreferencesKey("generation")
        private val KEY_TEAM = stringPreferencesKey("team")
        private val KEY_JOB_ROLE = stringPreferencesKey("job_role")
        private val KEY_ROLE = stringPreferencesKey("role")
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        // 온보딩 전 임시 저장용
        private val KEY_TEMP_OAUTH_TOKEN = stringPreferencesKey("temp_oauth_token")
        private val KEY_TEMP_OAUTH_PROVIDER = stringPreferencesKey("temp_oauth_provider")
    }

    suspend fun saveLoginData(
        userId: Long,
        name: String,
        email: String,
        generation: String,
        team: String,
        jobRole: String,
        role: String,
        accessToken: String,
        refreshToken: String
    ) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
            preferences[KEY_NAME] = name
            preferences[KEY_EMAIL] = email
            preferences[KEY_GENERATION] = generation
            preferences[KEY_TEAM] = team
            preferences[KEY_JOB_ROLE] = jobRole
            preferences[KEY_ROLE] = role
            preferences[KEY_ACCESS_TOKEN] = accessToken
            preferences[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    val userId: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[KEY_USER_ID]
    }

    val name: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_NAME]
    }

    val email: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_EMAIL]
    }

    val generation: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_GENERATION]
    }

    val team: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_TEAM]
    }

    val jobRole: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_JOB_ROLE]
    }

    val role: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_ROLE]
    }

    val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_ACCESS_TOKEN]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_REFRESH_TOKEN]
    }

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // 온보딩 전 임시 OAuth 토큰 저장
    suspend fun saveTempOAuthToken(token: String, provider: String) {
        dataStore.edit { preferences ->
            preferences[KEY_TEMP_OAUTH_TOKEN] = token
            preferences[KEY_TEMP_OAUTH_PROVIDER] = provider
        }
    }

    val tempOauthToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_TEMP_OAUTH_TOKEN]
    }

    val tempOauthProvider: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_TEMP_OAUTH_PROVIDER]
    }

    suspend fun clearTempOAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_TEMP_OAUTH_TOKEN)
            preferences.remove(KEY_TEMP_OAUTH_PROVIDER)
        }
    }
}
