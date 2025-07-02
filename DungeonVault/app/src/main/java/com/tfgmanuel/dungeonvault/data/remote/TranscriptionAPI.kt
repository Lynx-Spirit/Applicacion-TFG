package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.APIResponse
import com.tfgmanuel.dungeonvault.data.model.CleanInfo
import com.tfgmanuel.dungeonvault.data.model.Note
import com.tfgmanuel.dungeonvault.data.model.TranscribeInfo
import com.tfgmanuel.dungeonvault.data.model.TranscriptionStart
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * Interfaz que define las llamadas a la API relacioandas con la gestión de las notas de la campaña.
 * Usa Retrofit para mapear las peticiones HTTP.
 */
interface TranscriptionAPI {
    /**
     * Iniciar la transcripción
     *
     * @param campaignID Identificación de la campaña en la que se van a realizar la transcripción
     * @param token Token de autorización del usuario
     *
     * @return [Response] que contiene la información de la transcripción recién creada.
     */
    @POST("transcription/start")
    suspend fun initTranscription(
        @Query("campaign_id") campaignId: Int,
        @Header("Authorization") token: String
    ): Response<TranscriptionStart>

    /**
     * Realiza la transcripción del fichero pasado como parámetro.
     *
     * @param transcribeInfo Objecto que contiene la infotermación necesara para poder realizar la transcripción.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene un mensaje con el resultado.
     */
    @PUT("transcription/transcribe")
    suspend fun transcribe(
        @Body transcribeInfo: TranscribeInfo,
        @Header("Authorization") token: String
    ): Response<APIResponse>

    /**
     * Realiza la limpieza completa y el resumen de la transcripción del fichero pasado como parámetro
     *
     * @param cleanInfo Información necesaria para poder realizar la operación
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene la información del resumen recién creado.
     */
    @PUT("transcription/clean")
    suspend fun clean(
        @Body cleanInfo: CleanInfo,
        @Header("Authorization") token: String
    ): Response<APIResponse>
}