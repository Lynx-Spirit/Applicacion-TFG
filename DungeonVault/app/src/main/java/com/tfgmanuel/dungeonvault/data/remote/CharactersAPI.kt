package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.APIResponse
import com.tfgmanuel.dungeonvault.data.model.Character
import com.tfgmanuel.dungeonvault.data.model.CharacterUpdate
import com.tfgmanuel.dungeonvault.data.model.CreateCharacter
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
 * Interfaz que define las llamadas a la API relacionadas con la gestión de los personajes de la campaña
 * Utiliza Retrofit para mapear las peticiones HTTP.
 */
interface CharactersAPI {

    /**
     * Creación de un nuevo personaje
     *
     * @param createCharacter Información necesaria para la creación del personaje.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene el personaje creado ([Character])
     */
    @POST("characters/new")
    suspend fun createCharacter(
        @Body createCharacter: CreateCharacter,
        @Header("Authorization") token: String
    ): Response<Character>

    /**
     * Obtiene los detalles de un personaje específico por su ID.
     *
     * @param id Identificador del personaje.
     * @param token token de autorización del usuario
     *
     * @return [Response] que contiene el personaje obtenido ([Character])
     */
    @GET("characters/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Character>

    /**
     * Obtención de todos los personajes de la partida.
     *
     * @param campaignID Identificador de la partida la cual se quieren obtener los personajes.
     * @param token token de autorización del usuario
     *
     * @return [Response] que contiene un [List] de todos los personajes de la partida
     */
    @GET("characters/")
    suspend fun getCampaignCharacters(
        @Query("campaign_id") campaignID: Int,
        @Header("Authorization") token: String
    ): Response<List<Character>>

    /**
     * Actualización de los datos de un personaje.
     *
     * @param id Identificador del personaje del que se quiere actualizar los datos.
     * @param characterUpdate Información necesaria para actualizar los datos del personaje.
     * @param token Token de autorización del usuario.
     */
    @PUT("characters/{id}/update")
    suspend fun updateCharacter(
        @Path("id") id: Int,
        @Body characterUpdate: CharacterUpdate,
        @Header("Authorization") token: String
    ): Response<Character>

    /**
     * Eliminación de un personaje concreto.
     *
     * @param id Identificador del persoanje del que se quiere actualizar los datos.
     * @param token Token de autorización del usuario.
     */
    @DELETE("characters/{id}/delete")
    suspend fun deleteCharacter(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<APIResponse>
}