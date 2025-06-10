package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.APIResponse
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.CreateCampaign
import com.tfgmanuel.dungeonvault.data.model.KickInformation
import com.tfgmanuel.dungeonvault.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define las llamadas a la API relacionadas con la gestión de las campañas.
 * Utiliza Retrofit para mapear las peticiones HTTP.
 */
interface CampaignAPI {

    /**
     * Creación de una campaña nueva
     *
     * @param createCampaign Información necearia para la creación de la campaña.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene la campaña creada ([Campaign]).
     */
    @POST("campaigns/new")
    suspend fun createCampaign(
        @Body createCampaign: CreateCampaign,
        @Header("Authorization") token: String
    ): Response<Campaign>

    /**
     * Obtiene los detalles de una campaña específica por su ID.
     *
     * @param id Identificador de la campaña.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene la campaña creada ([Campaign])
     */
    @GET("campaigns/{id}")
    suspend fun getCampaign(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Campaign>

    /**
     * Obtiene todos los usuarios de una campaña específica por su ID.
     *
     * @param id Identificador de la campaña.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene un [List] de [User] de la campaña.
     */
    @GET("campaigns/{id}/members")
    suspend fun getMembers(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<List<User>>

    /**
     * Obtiene todas las campañas asociadas al usuario autenticado.
     *
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene una lista de campañas ([List]<[Campaign]>).
     */
    @GET("campaigns/")
    suspend fun getCampaigns(
        @Header("Authorization") token: String
    ): Response<List<Campaign>>

    /**
     * Actualiza una campaña existente.
     *
     * @param id Identificador de la campaña.
     * @param createCampaign Nuevos datos para actualizar la campaña.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene la campaña actualizada ([Campaign])
     */
    @PUT("campaigns/{id}/update")
    suspend fun updateCampaign(
        @Path("id") id: Int,
        @Body createCampaign: CreateCampaign,
        @Header("Authorization") token: String
    ): Response<Campaign>

    /**
     * Inserta un nuevo usuario a una campaña existente mediante un código de invitación.
     *
     * @param inviteCode Código de invitación para unirse a la campaña.
     * @param token Token de acceso del usuario.
     *
     * @return [Response] que contiene la campaña actualizada ([Campaign]) con el nuevo usuario.
     */
    @PATCH("campaigns/new-user")
    suspend fun addUser(
        @Query("invite_code") inviteCode: String,
        @Header("Authorization") token: String
    ): Response<Campaign>

    /**
     * Elimina a un usuario autenticado de una campaña específica.
     *
     * @param id Identificador de la campaña
     * @param token Token de acceso del usuario.
     *
     * @return [Response] que contiene un [APIResponse] con el resultado de la operación.
     */
    @PATCH("campaigns/{id}/remove-user")
    suspend fun removeUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<APIResponse>

    /**
     * Echa a un usuario específico de una campaña.
     *
     * @param id Identificador de la campaña.
     * @param kickInformation Objeto que contiene toda la informacíon relativa para echar al usaurio.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] con el mensaje de respuesta de la API.
     */
    @PATCH("campaigns/{id}/kick-user")
    suspend fun kickUSer(
        @Path("id") id: Int,
        @Body kickInformation: KickInformation,
        @Header("Authorization") token: String
    ): Response<APIResponse>

    /**
     * Elimina una campaña específica.
     *
     * @param id Identificador de la campaña.
     * @param token Token de autorización del usuario.
     *
     * @return [Response] que contiene un [APIResponse] con el resultado de la operación.
     */
    @DELETE("campaigns/{id}/delete")
    suspend fun deleteCampaign(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<APIResponse>
}