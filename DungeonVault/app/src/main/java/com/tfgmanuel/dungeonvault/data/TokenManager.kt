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

// Datastore donde se va a almacenar la inforamción.
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

    /**
     * Flujo que emite el token de acceso almacenado en [dataStore].
     * Este flujo puede contener null en caso de que no se haya guardado ningún token.
     */
    private val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    /**
     * Flujo que emite el token de refresco  almacenado en [dataStore].
     * Este flujo puede contener null en caso de que no se haya guardado ningún token.
     */
    private val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    /**
     * Flujo que emite el tipo de token almacenado en [dataStore].
     * Este flujo puede contener null en caso de que no se haya guardado ningún token.
     */
    private val tokenType: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_TYPE]
    }

    /**
     * Flujo que emite el identificador del usuario almacenado en [dataStore].
     * Este flujo puede contener null en caso de que no se haya guardado ningún token.
     */
    private val userID: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    /**
     * Devuelve un flujo que emite el token de acceso almacenado en [dataStore].
     *
     * @return[Flow] que emite el token de acceso como [String].
     */
    fun getAccessToken(): Flow<String?> {
        return accessToken
    }

    /**
     * Devuelve un flujo que emite el token de refresco almacenado en [dataStore].
     *
     * @return[Flow] que emite el token de refresco como [String].
     */
    fun getRefreshToken(): Flow<String?> {
        return refreshToken
    }

    /**
     * Devuelve un flujo que emite el tipo de token almacenado en [dataStore].
     *
     * @return[Flow] que emite el tipo de token como [String].
     */
    fun getTokenType(): Flow<String?> {
        return tokenType
    }

    /**
     * Devuelve un flujo que emite el identificador de usuario almacenado en [dataStore].
     *
     * @return[Flow] que emite el identificador de usuario como [Int].
     */
    fun getUserID(): Flow<Int?> {
        return userID
    }

    /**
     * Almacena solamente los tokens en [dataStore].
     *
     * @param accessToken Token de acceso que se quiere almacenar.
     * @param refreshToken Token de refresco que se quiere almacenar.
     * @param tokenType Tipo de token a almacenar.
     */
    suspend fun saveTokens(accessToken: String, refreshToken: String, tokenType: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[TOKEN_TYPE] = tokenType
        }
    }

    /**
     * Almacena toda la información en [dataStore].
     *
     * @param accessToken Token de acceso a almacenar.
     * @param refreshToken Token de refresco que se quiere almacenar.
     * @param tokenType Tipo de token a almacenar.
     * @param userID Identificador de usuario que se quiere almacenar.
     */
    suspend fun saveAll(accessToken: String, refreshToken: String, tokenType: String, userID: Int) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[TOKEN_TYPE] = tokenType
            preferences[USER_ID] = userID
        }
    }

    /**
     * Elimina toda la información del [dataStore]
     */
    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}