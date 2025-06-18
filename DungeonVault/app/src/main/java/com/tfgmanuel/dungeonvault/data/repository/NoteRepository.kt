package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.NoteDAO
import com.tfgmanuel.dungeonvault.data.model.CreateNote
import com.tfgmanuel.dungeonvault.data.model.UpdateNote
import com.tfgmanuel.dungeonvault.data.remote.NotesAPI
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val fileRepository: FileRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val notesAPI: NotesAPI,
    private val noteDAO: NoteDAO
) {
    /**
     * Creación de una nueva nota por parte de un usuario autenticado.
     *
     * @param campaignID Identificación de la campaña.
     * @param title Título de la nota que se va a crear.
     * @param content Contenido de la nota que se va a crear.
     * @param visibility Visibilidad de la nueva nota.
     * @param context Contexto de la aplicación.
     *
     * @return [Result] con el resultado de la ejecución
     */
    suspend fun createNote(
        campaignID: Int,
        title: String,
        content: String,
        visibility: Boolean,
        context: Context
    ): Result<String> {
        val contentFilename = fileRepository.uploadText(content, context)

        val note = CreateNote(
            campaign_id = campaignID,
            title = title,
            file_name = contentFilename,
            visibility = visibility
        )

        return firstAttemptCreate(note)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptCreate(note) }
    }

    /**
     * Primer intento de crear una nota.
     * En caso de que la API devuelva 401, se devolverá null para poder refrescar los tokens.
     *
     * @param note Datos necesarios para poder crear la nota
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptCreate(note: CreateNote): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.createNote(note, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.insert(response.body()!!)
            return Result.success("Nota creada correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento para crear la nota tras haber refrescado los tokens.
     *
     * @param note Información necesaria para crear la nota.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptCreate(note: CreateNote): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.createNote(note, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.insert(response.body()!!)
            return Result.success("Nota creada correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Obtención de todas las notas de la campaña.
     *
     * @param campaignID Identificador de la campaña de la que se quiere obtener todas las notas
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun getAllNotes(campaignID: Int): Result<String> {
        return firstAttemptGetAllNotes(campaignID)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptGetAllNotes(campaignID) }
    }

    /**
     * Primer intento de obtener todas las notas de la campaña
     * En caso de que la API devuelva 401, se devolverá null para poder refrescar los tokens.
     *
     * @param campaignID Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptGetAllNotes(campaignID: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.getCampaignNotes(campaignID, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.insertAll(response.body()!!)
            return Result.success("Notas insertadas correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de obtener todas las notas de la campaña, tras haber refrescado los tokens.
     *
     * @param campaignID Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptGetAllNotes(campaignID: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.getCampaignNotes(campaignID, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.insertAll(response.body()!!)
            return Result.success("Notas insertadas correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Actualización de los datos de una nota determinada
     *
     * @param id Identificador de la nota.
     * @param title Nuevo título de la nota.
     * @param content Nuevo contenido de la nota.
     * @param fileName Nombre del fichero de texto del contenido de la nota.
     * @param visibility Nueva visibilidad de la nota.
     * @param context Contexto de la app.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun updateNote(
        id: Int,
        title: String,
        content: String,
        fileName: String,
        visibility: Boolean,
        context: Context
    ): Result<String> {
        if (content.isNotBlank()) {
            fileRepository.updateText(fileName, content, context)
        }

        val note = UpdateNote(
            title = title,
            file_name = fileName,
            visibility = visibility
        )

        return firstAttemptUpdate(id, note)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptUpdate(id, note) }
    }

    /**
     * Primer intento de actualizar la nota.
     * En caso de devolver 401, se devolverá null para poder refrescar los tokens del usuario.
     *
     * @param id Identificador de la nota.
     * @param note Datos actualizados de la nota.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptUpdate(id: Int, note: UpdateNote): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.updateNote(id, note, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.updateNote(response.body()!!)
            return Result.success("Nota actualizada correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento para actualizar la nota tras haber refrescado los tokens del usuario.
     *
     * @param id Identificador de la nota.
     * @param note Información actulizada de la nota.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptUpdate(id: Int, note: UpdateNote): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.updateNote(id, note, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.updateNote(response.body()!!)
            return Result.success("Nota actualizada correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Elimina una nota concreta
     *
     * @param id  Identificador de la nota a eliminar.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun deleteNote(id: Int): Result<String> {
        return firstAttemptDelete(id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptDelete(id) }
    }

    /**
     * Primer intento para eliminar la nota
     * En caso de que la APi devuelva 401, se devolverá null para poder refrescar los tokens del usuario.
     *
     * @param id Identificador de la nota que se quiere actualizar.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptDelete(id: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.deleteNote(id, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.deleteNote(id)
            return Result.success("Nota eliminada correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento para actualizar la nota una vez refrescado los tokens del usuario
     *
     * @param id Identificador de la nota que se quiere actualizar.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private  suspend fun secondAttemptDelete(id: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = notesAPI.deleteNote(id, "Bearer $access")

        if (response.isSuccessful) {
            noteDAO.deleteNote(id)
            return Result.success("Nota eliminada correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }
}