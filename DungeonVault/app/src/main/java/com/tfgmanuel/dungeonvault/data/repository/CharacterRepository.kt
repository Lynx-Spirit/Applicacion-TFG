package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import android.net.Uri
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CharacterDAO
import com.tfgmanuel.dungeonvault.data.model.CharacterUpdate
import com.tfgmanuel.dungeonvault.data.model.CreateCharacter
import com.tfgmanuel.dungeonvault.data.remote.CharactersAPI
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val fileRepository: FileRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val characterAPI: CharactersAPI,
    private val characterDAO: CharacterDAO
) {
    /**
     * Creación de un nuevo personaje por parte de un usario autenticado.
     *
     * @param campaignID Identificación de la campaña.
     * @param name Nombre del personaje que se quiere crear.
     * @param description Descripción del personaje.
     * @param backstory Backstory del personaje
     * @param imgUri Uri de la imagen para el personaje.
     * @param visibility Visibilidad.
     *
     * @return [Result] con el resultado de la ejecución
     */
    suspend fun createCharacter(
        campaignID: Int,
        name: String,
        description: String,
        backstory: String,
        imgUri: Uri,
        visibility: Boolean,
        context: Context
    ): Result<String> {
        var imgName = ""
        val backstoryFilename = fileRepository.uploadText(backstory, context)

        if (imgUri != Uri.EMPTY) {
            imgName = fileRepository.uploadImage(imgUri, context)
        }

        val character = CreateCharacter(
            campaign_id = campaignID,
            name = name,
            description = description,
            filename_backstory = backstoryFilename,
            img_name = imgName,
            visibility = visibility
        )

        return firstAttemptCreate(character)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptCreate(character) }
    }

    /**
     * Primer intento de crear un personaje
     * En caso de que devuelva la API 401, se retornará null para poder refrescar los tokens.
     *
     * @param character Datos de la campaña a crear.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptCreate(character: CreateCharacter): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.createCharacter(character, "Bearer $access")

        if (response.isSuccessful) {
            characterDAO.insert(response.body()!!)
            return Result.success("Personaje insertado correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de crear un personaje tras haber refrescado los tokens del usuario.
     *
     * @param character Datos de la campaña a crear
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptCreate(character: CreateCharacter): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.createCharacter(character, "Bearer $access")

        if (response.isSuccessful) {
            characterDAO.insert(response.body()!!)
            return Result.success("Personaje insertado correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Obtención de todos los personajes de la camapaña
     *
     * @param campaignID Identificador de la campaña de la que se quiere obtener los personajes.
     */
    suspend fun getAllCharacters(campaignID: Int): Result<String> {
        return firstAttemptGetAllCharacters(campaignID)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptGetAllCharacters(campaignID) }
    }

    /**
     * Primer intento para obtener todos los personajes de la campaña.
     * En caso de que la APi devuelva 401, se retornará null para refrescar los tokens del usuario.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptGetAllCharacters(campaignID: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.getCampaignCharacters(campaignID, "Bearer ${access}")

        if (response.isSuccessful) {
            characterDAO.insertAll(response.body()!!)
            return Result.success("Personajes obtenidos correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de obtener todos los personajes de la campaña tras hbaer refrescado los tokens.
     *
     * @param campaignID Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptGetAllCharacters(campaignID: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.getCampaignCharacters(campaignID, "Bearer ${access}")

        if (response.isSuccessful) {
            characterDAO.insertAll(response.body()!!)
            return Result.success("Personajes obtenidos correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Actualiación de los datos de un personaje.
     *
     * @param id Identificador del personaje.
     * @param name Nuevo nombre del personaje.
     * @param description Nueva descripción del personaje.
     * @param backstory Nueva backstory del personaje.
     * @param backstoryFilename Nombre del fichero de texto del backstory.
     * @param imgUri Nueva imagen del personaje.
     * @param visibility Nueva visibilidad del personaje.
     * @param context Contexto de la app.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun updateCharacter(
        id: Int,
        name: String,
        description: String,
        backstory: String,
        backstoryFilename: String,
        imgUri: Uri,
        visibility: Boolean,
        context: Context
    ): Result<String> {
        var imgName = ""

        if (backstory.isNotBlank()) {
            fileRepository.updateText(backstoryFilename, backstory, context)
        }

        if (imgUri != Uri.EMPTY) {
            imgName = fileRepository.uploadImage(imgUri, context)
        }

        val character = CharacterUpdate(
            name = name,
            description = description,
            filename_backstory = backstoryFilename,
            img_name = imgName,
            visibility = visibility
        )

        return firstAttemptUpdateCharacter(id, character)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptUpdateCharacter(id, character) }
    }

    /**
     * Primer intento para actualizar los datos el personaje.
     * En caso de que la API devuelva 401, se devovlerá null para poder refrescar los tokens del usuario.
     *
     * @param id Identificación del personaje.
     * @param characterInfo Información necesaria para actualizar los datos del personaje.
     *
     * @return [Result] con un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptUpdateCharacter(
        id: Int,
        characterInfo: CharacterUpdate
    ): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.updateCharacter(id, characterInfo, "Bearer $access")

        if(response.isSuccessful) {
            characterDAO.updateCharacter(response.body()!!)
            return Result.success("Personaje actualizado correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento para actualizar los datos el personaje tras haber refrescado los tokens del usuario.
     *
     * @param id Identificación del personaje.
     * @param characterInfo Información necesaria para actualizar los datos del personaje.
     *
     * @return [Result] con un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptUpdateCharacter(
        id: Int,
        characterInfo: CharacterUpdate
    ): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.updateCharacter(id, characterInfo, "Bearer $access")

        if(response.isSuccessful) {
            characterDAO.updateCharacter(response.body()!!)
            return Result.success("Personaje actualizado correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Eliminación de un personaje.
     *
     * @param id Identificador del personaje que se quiere eliminar.
     *
     * @return [Result] con un [String] con el resultado de la ejecución.
     */
    suspend fun deleteCharacter(id: Int): Result<String> {
        return firstAttemptDelete(id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptDelete(id) }
    }

    /**
     * Primer intento para eliminar el personaje seleccionado.
     *
     * @param id Identificador el personaje.
     *
     * @return [Result] con un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptDelete(id: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.deleteCharacter(id, "Bearer $access")

        if(response.isSuccessful) {
            characterDAO.deleteCharacter(id)
            return Result.success("Personaje eliminado correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento para eliminar el personaje seleccionado.
     *
     * @param id Identificador el personaje.
     *
     * @return [Result] con un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptDelete(id: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = characterAPI.deleteCharacter(id, "Bearer $access")

        if(response.isSuccessful) {
            characterDAO.deleteCharacter(id)
            return Result.success("Personaje eliminado correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }
}