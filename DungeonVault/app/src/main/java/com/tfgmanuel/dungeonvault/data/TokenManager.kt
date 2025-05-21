package com.tfgmanuel.dungeonvault.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private  val dataStore = context.dataStore
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    private val TOKEN_TYPE = stringPreferencesKey("token_type")
    private val USER_ID = intPreferencesKey("user_id")

    private val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    private val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    private val tokenType: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_TYPE]
    }

    private val userID: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    fun getAccessToken(): Flow<String?> {
        return accessToken
    }

    fun getRefreshToken(): Flow<String?> {
        return refreshToken
    }

    fun getTokenType(): Flow<String?> {
        return tokenType
    }

    fun getUserID(): Flow<Int?> {
        return userID
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String, tokenType: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[TOKEN_TYPE] = tokenType
        }
    }

    suspend fun saveAll(accessToken: String, refreshToken: String, tokenType: String, userID: Int) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[TOKEN_TYPE] = tokenType
            preferences[USER_ID] = userID
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}