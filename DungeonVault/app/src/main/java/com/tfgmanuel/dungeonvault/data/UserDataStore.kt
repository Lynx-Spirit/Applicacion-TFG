package com.tfgmanuel.dungeonvault.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_info")

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    private val EMAIL = stringPreferencesKey("email")
    private val AVATAR = stringPreferencesKey("avatar")
    private val NICKNAME = stringPreferencesKey("nickname")


    private val email: Flow<String?> = dataStore.data.map { preferences ->
        preferences[EMAIL]
    }

    private val avatar: Flow<String?> = dataStore.data.map { preferences ->
        preferences[AVATAR]
    }

    private val nickname: Flow<String?> = dataStore.data.map { preferences ->
        preferences[NICKNAME]
    }

    fun getEmail(): Flow<String?> {
        return email
    }

    fun getAvatar(): Flow<String?> {
        return avatar
    }

    fun getNickname(): Flow<String?> {
        return nickname
    }

    suspend fun saveInformation(email: String, avatar: String, nickname: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
            preferences[AVATAR] = avatar
            preferences[NICKNAME] = nickname
        }
    }

    suspend fun deleteInformation() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}