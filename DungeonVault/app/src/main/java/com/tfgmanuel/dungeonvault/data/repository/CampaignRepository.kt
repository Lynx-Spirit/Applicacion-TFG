package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import android.net.Uri
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.model.CreateCampaign
import com.tfgmanuel.dungeonvault.data.remote.CampaignAPI
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CampaignRepository @Inject constructor(
    private val imgRepository: ImgRepository,
    private val campaignAPI: CampaignAPI,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val campaignDAO: CampaignDAO
) {

    suspend fun createCampaign(
        title: String,
        description: String,
        imgUri: Uri,
        context: Context
    ): Result<String> {
        var fileName = ""

        if (imgUri != Uri.EMPTY) {
            fileName = imgRepository.uploadImage(imgUri, context)
        }

        val campaign = CreateCampaign(title, description, fileName)

        return firstAttemptCreate(campaign)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptCreate(campaign) }
    }

    private suspend fun firstAttemptCreate(campaign: CreateCampaign): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.createCampaign(campaign, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            return Result.success("Campaña insertada con éxito")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptCreate(campaign: CreateCampaign): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.createCampaign(campaign, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            return Result.success("Campaña insertada tras refresh")
        } else {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }
    }

    suspend fun getAllCampaigns(): Result<String> {
        return firstAttemptGet()
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptGet() }
    }

    private suspend fun firstAttemptGet(): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.getCampaigns("Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteAll()
            campaignDAO.insertAll(response.body()!!)
            return Result.success("Camapañas insertadas correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptGet(): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.getCampaigns("Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteAll()
            campaignDAO.insertAll(response.body()!!)
            return Result.success("Camapañas insertadas correctamente")
        }

        return Result.failure(Exception("Fallo al cargar las camapañas"))
    }

    suspend fun updateCampaign(
        id: Int,
        title: String = "",
        description: String = "",
        originalFileName: String,
        imgUri: Uri = Uri.EMPTY,
        context: Context
    ): Result<String> {
        var fileName = originalFileName

        if (imgUri != Uri.EMPTY) {
            fileName = imgRepository.uploadImage(imgUri, context)
        }
        val campaign = CreateCampaign(title, description, fileName)

        return firstAttemptUpdate(id = id, campaign = campaign)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptUpdate(id = id, campaign = campaign) }
    }

    private suspend fun firstAttemptUpdate(id: Int, campaign: CreateCampaign): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.updateCampaign(id, campaign, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            campaignDAO.insert(response.body()!!)
            return Result.success("Campaña actualizada correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptUpdate(id: Int, campaign: CreateCampaign): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.updateCampaign(id, campaign, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            campaignDAO.insert(response.body()!!)
            return Result.success("Campaña actualizada correctamente")
        }

        return Result.failure(Exception("No se ha podido actualizar la campaña"))
    }

    suspend fun insertUser(inviteCode: String): Result<String> {
        return firstAttemptInsertUser(inviteCode = inviteCode)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptInsertUser(inviteCode = inviteCode) }
    }

    private suspend fun firstAttemptInsertUser(inviteCode: String): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.addUser(inviteCode, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            return Result.success("Invitación aceptada")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptInsertUser(inviteCode: String): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.addUser(inviteCode, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            return Result.success("Invitación aceptada")
        }

        return Result.failure(Exception("No has podido entrar a la partida"))
    }

    suspend fun removeUser(id: Int): Result<String> {
        return firstAttemptRemoveUser(id = id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptRemoveUser(id = id) }
    }

    private suspend fun firstAttemptRemoveUser(id: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.removeUser(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            return Result.success("Camapaña eliminada de forma exitosa")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptRemoveUser(id: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.removeUser(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            return Result.success("Camapaña eliminada de forma exitosa")
        }

        return Result.failure(Exception("No has salirte de la partida"))
    }

    suspend fun removeCampaign(id: Int): Result<String> {
        return firstAttemptRemoveCampaign(id = id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptRemoveCampaign(id = id) }
    }

    private suspend fun firstAttemptRemoveCampaign(id: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.deleteCampaign(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            return Result.success("Camapaña eliminada de forma exitosa")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun  secondAttemptRemoveCampaign(id: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.deleteCampaign(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            return Result.success("Camapaña eliminada de forma exitosa")
        }

        return Result.failure(Exception("La camapaña no se ha podido eliminar"))
    }
}