package com.tfgmanuel.dungeonvault.data.repository

import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.NoteDAO
import com.tfgmanuel.dungeonvault.data.model.CleanInfo
import com.tfgmanuel.dungeonvault.data.model.TranscribeInfo
import com.tfgmanuel.dungeonvault.data.remote.TranscriptionAPI
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TranscriptionRepository @Inject constructor(
    private val transcriptionAPI: TranscriptionAPI,
    private  val authRepository: AuthRepository,
    private val noteDAO: NoteDAO,
    private val tokenManager: TokenManager
) {
    /**
     * Inicia la transcripción.
     *
     * @param campaignID Identificador de la campaña.
     *
     * @return [Result] con el nombre del fichero de texto creado.
     */
    suspend fun createTranscription(
        campaignID: Int
    ): Result<String> {
        return firstAttemptCreate(campaignID)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptCreate(campaignID) }
    }

    /**
     * Primer intento te crear la transcripción.
     * En caso de que la API devuelva 401, se devolverá null para poder refrescar los tokens del usuario.
     *
     * @param campaignID Identificación de la campaña.
     *
     * @return [Result] que contiene un [String] con el nombre del fichero de texto creado.
     */
    private suspend fun firstAttemptCreate(
        campaignID: Int
    ): Result<String>? {
        val access = tokenManager.getAccessToken().first()!!
        val response = transcriptionAPI.initTranscription(campaignID, "Bearer $access")

        if (response.isSuccessful) {
            val data = response.body()!!
            return Result.success(data.filename + "," + data.summary)
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de crear una nueva transcripción tras haber refrescado los tokens del usuario.
     *
     * @param campaignID Identificación de la campaña.
     *
     * @return [Result] que contiene un [String] con el nombre del fichero de texto creado.
     */
    private suspend fun secondAttemptCreate(
        campaignID: Int
    ): Result<String> {
        val access = tokenManager.getAccessToken().first()!!
        val response = transcriptionAPI.initTranscription(campaignID, "Bearer $access")

        if (response.isSuccessful) {
            val data = response.body()!!
            return Result.success(data.filename + "," + data.summary)
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Realiza la transcirpción del fichero pasado como parámetro.
     *
     * @param campaignID Identificador de la campaña
     * @param audio Nombre del fichero de audio que se quiere transcribir.
     * @param filename Nombre del fichero de texto donde se almacenará el resultado.
     * @param summary Nombre del archivo de texto donde se almacenará el resumen.
     *
     * @return [Result] con el resultado de la ejecución
     */
    suspend fun transcribe(
        campaignID: Int,
        audio: String,
        filename: String,
        summary : String
    ): Result<String> {
        val transcribeInfo = TranscribeInfo(
            campaign_id = campaignID,
            audio = audio,
            filename = filename,
            summary = summary
        )

        return firstAttemptTranscribe(transcribeInfo)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptTranscribe(transcribeInfo) }
    }

    /**
     * Primer intento de transcribir el audio.
     * En caso de que la API devuelva 401, se devolverá null para poder refrescar los tokens.
     *
     * @param transcribeInfo Información necesaria para poder realizar la transcripción
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptTranscribe(
        transcribeInfo: TranscribeInfo
    ): Result<String>? {
        val access = tokenManager.getAccessToken().first()!!
        val response = transcriptionAPI.transcribe(transcribeInfo, "Bearer $access")

        if (response.isSuccessful) {
            return Result.success("Transcripción realizada correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de transcribir el archivo tras haber refrescado los tokens del usuario.
     *
     * @param transcribeInfo Información necesaria para poder realizar la transcripción
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptTranscribe(
        transcribeInfo: TranscribeInfo
    ): Result<String> {
        val access = tokenManager.getAccessToken().first()!!
        val response = transcriptionAPI.transcribe(transcribeInfo, "Bearer $access")

        if (response.isSuccessful) {
            return Result.success("Transcripción realizada correctamente")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

    /**
     * Realiza la limpieza completa y el resumen de la transcripción del fichero pasado como parámetro
     *
     * @param campaignID Identificador de la campaña
     * @param filename Fichero de texto de la transcripción de la cual se va a realizar el resumen.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun cleanTranscription(
        campaignID: Int,
        filename: String
    ): Result<String> {
        val cleanInfo = CleanInfo(
            campaign_id = campaignID,
            filename = filename
        )

        return firstAttemptCleanTranscription(cleanInfo)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptCleanTranscription(cleanInfo) }
    }

    /**
     * Primer intento de realizar la limpieza y el resumen.
     *
     * @param cleanInfo Información necesaria para poder realizar la operación
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptCleanTranscription (
        cleanInfo: CleanInfo
    ): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = transcriptionAPI.clean(cleanInfo, "Bearer $access")

        if (response.isSuccessful) {
            return Result.success("Limpieza realizada de forma correcta.")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y útlimo intento de realizar la limpieza y el resumen tras haber refrescado los tokens del usuario.
     *
     * @param cleanInfo Información necesaria para poder realizar la operación
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptCleanTranscription(
        cleanInfo: CleanInfo
    ): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = transcriptionAPI.clean(cleanInfo, "Bearer $access")

        if (response.isSuccessful) {
            return Result.success("Limpieza realizada de forma correcta.")
        }

        val detail = getErrorFromApi(response)
        return Result.failure(Exception(detail))
    }

}