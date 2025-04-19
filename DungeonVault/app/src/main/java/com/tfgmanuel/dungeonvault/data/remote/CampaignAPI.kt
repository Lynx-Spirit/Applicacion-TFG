package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.APIResponse
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.CreateCampaign
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

interface CampaignAPI {

    @POST("campaigns/")
    suspend fun createCampaign(
        @Body createCampaign: CreateCampaign,
        @Header("Authorization") token: String
    ):Response<Campaign>

    @GET("campaigns/{id}")
    suspend fun getCampaign(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ):Response<Campaign>

    @GET("campaigns/")
    suspend fun getCampaigns(
        @Header("Authorization") token: String
    ):Response<List<Campaign>>

    @PUT("campaigns/{id}/update")
    suspend fun updateCampaign(
        @Path("id") id: Int,
        @Body createCampaign: CreateCampaign,
        @Header("Authorization") token: String
    ):Response<Campaign>

    @PATCH("campaigns/new-user")
    suspend fun addUser(
        @Query("invite_code") inviteCode: String,
        @Header("Authorization") token: String
    ):Response<Campaign>

    @PATCH("campaigns/{id}/remove-user")
    suspend fun removeUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ):Response<APIResponse>

    @DELETE("campaigns/{id}")
    suspend fun deleteCampaign(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ):Response<APIResponse>
}