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

// // Datastore donde se va a almacenar la inforamción.
private val Context.dataStore by preferencesDataStore(name = "user_info")

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    private val EMAIL = stringPreferencesKey("email")
    private val AVATAR = stringPreferencesKey("avatar")
    private val NICKNAME = stringPreferencesKey("nickname")

    /**
     * Flujo que emite el email del usuario almacenado en [dataStore].
     * Este flujo puede contener null en caso de que no se haya guardado ningún token.
     */
    private val email: Flow<String?> = dataStore.data.map { preferences ->
        preferences[EMAIL]
    }

    /**
     * Flujo que emiteel nombre del fichero de imagen del usuario almacenado en [dataStore].
     * Este flujo puede contener null en caso de que no se haya guardado ningún token.
     */
    private val avatar: Flow<String?> = dataStore.data.map { preferences ->
        preferences[AVATAR]
    }

    /**
     * Flujo que emite el apodo del usuario almacenado en [dataStore].
     * Este flujo puede contener null en caso de que no se haya guardado ningún token.
     */
    private val nickname: Flow<String?> = dataStore.data.map { preferences ->
        preferences[NICKNAME]
    }

    /**
     * Devuelve un flujo que emite el email del usuario almacenado en [dataStore].
     *
     * @return[Flow] que emite email del usuario como [String].
     */
    fun getEmail(): Flow<String?> {
        return email
    }

    /**
     * Devuelve un flujo que emite el nombre de fichero de imagen del usuario almacenado en [dataStore].
     *
     * @return[Flow] que emite el nombre de fichero de imagen del usuario como [String].
     */
    fun getAvatar(): Flow<String?> {
        return avatar
    }

    /**
     * Devuelve un flujo que emite el apodo del usuario almacenado en [dataStore].
     *
     * @return[Flow] que emite apodo del usuario como [String].
     */
    fun getNickname(): Flow<String?> {
        return nickname
    }

    /**
     * Almacena toda la información del usuario en el [dataStore].
     *
     * @param email Email del usuario que se quiere almacenar.
     * @param avatar Nombre de fichero de imagen del usuario que se quiere almacenar.
     * @param nickname Apodo del usuario que se quiere almacenar.
     */
    suspend fun saveInformation(email: String, avatar: String, nickname: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
            preferences[AVATAR] = avatar
            preferences[NICKNAME] = nickname
        }
    }

    /**
     * Elimina toda la información del [dataStore]
     */
    suspend fun deleteInformation() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}