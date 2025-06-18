package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.APIResponse
import com.tfgmanuel.dungeonvault.data.model.Character
import com.tfgmanuel.dungeonvault.data.model.CharacterUpdate
import com.tfgmanuel.dungeonvault.data.model.CreateCharacter
import com.tfgmanuel.dungeonvault.data.model.CreateNote
import com.tfgmanuel.dungeonvault.data.model.Note
import com.tfgmanuel.dungeonvault.data.model.UpdateNote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define las llamadas a la API relacionadas con la gestión de las notas de la campaña.
 * Usa Retrofit para mapear las peticiones HTTP.
 */
interface NotesAPI {

    /**
     * Creación de una nueva nota.
     *
     * @param createNote Información necesaria para la creación de la nota.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene la información de la nota recién creada.
     */
    @POST("notes/new")
    suspend fun createNote(
        @Body createNote: CreateNote,
        @Header("Authorization") token: String
    ): Response<Note>

    /**
     * Obtiene los detalles de una nota específica por su ID.
     *
     * @param id Identificador de la nota.
     * @param token token de autorización del usuario
     *
     * @return [Response] que contiene la nota obtenida ([Note])
     */
    @GET("notes/{id}")
    suspend fun getNote(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Note>

    /**
     * Obtención de todos las notas de la partida.
     *
     * @param campaignID Identificador de la partida la cual se quieren obtener las notas.
     * @param token token de autorización del usuario
     *
     * @return [Response] que contiene un [List] de todas las notas de la partida
     */
    @GET("notes/")
    suspend fun getCampaignNotes(
        @Query("campaign_id") campaignID: Int,
        @Header("Authorization") token: String
    ): Response<List<Note>>

    /**
     * Actualización de los datos de una nota.
     *
     * @param id Identificador de la nota del que se quiere actualizar los datos.
     * @param updateNote Información necesaria para actualizar los datos de la nota.
     * @param token Token de autorización del usuario.
     */
    @PUT("notes/{id}/update")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Body updateNote: UpdateNote,
        @Header("Authorization") token: String
    ): Response<Note>

    /**
     * Eliminación de una nota concreta.
     *
     * @param id Identificador de la nota del que se quiere actualizar los datos.
     * @param token Token de autorización del usuario.
     */
    @DELETE("notes/{id}/delete")
    suspend fun deleteNote(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<APIResponse>
}