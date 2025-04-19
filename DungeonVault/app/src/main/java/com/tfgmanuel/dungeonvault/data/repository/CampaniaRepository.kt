package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.model.CreateCampaign
import com.tfgmanuel.dungeonvault.data.remote.CampaignAPI
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CampaniaRepository @Inject constructor(
    private val imgRepository: ImgRepository,
    private val campaignAPI: CampaignAPI,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val campaignDAO: CampaignDAO
) : ViewModel() {

    suspend fun create(title: String, description: String, imgUri: Uri, context: Context): Result<String> {
        try{
            val url = imgRepository.uploadImage(imgUri, context)
            val campaign = CreateCampaign(title, description, url)
            var access = tokenManager.getAccessToken().first()

            var response = campaignAPI.createCampaign(campaign,"Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.insert(response.body()!!)
                return Result.success("Campaña insertada con éxito")
            }

            if(response.code() != 401) {
                return Result.failure(Exception("Error al crear camapaña: ${response.message()}"))
            }

            val refresh = tokenManager.getRefreshToken().first()

            if(refresh == null) {
                return Result.failure(Exception("No hay token de refresco"))
            }

            val refreshSuccess = authRepository.refresh(refresh)

            if(refreshSuccess.isFailure) {
                return Result.failure(Exception("Refresh fallido"))
            }

            access = tokenManager.getAccessToken().first()
            response = campaignAPI.createCampaign(campaign, "Bearer $access")

            if (response.isSuccessful) {
                campaignDAO.insert(response.body()!!)
                return Result.success("Campaña insertada tras refresh")
            } else {
                return Result.failure(Exception("Error tras refresh: ${response.message()}"))
            }

        }catch(e: Exception) {
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun getAllCampaigns(): Result<String> {
        try {
            var access = tokenManager.getAccessToken().first()

            var response = campaignAPI.getCampaigns("Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteAll()
                campaignDAO.insertAll(response.body()!!)
                return Result.success("Camapañas insertadas correctamente")
            }

            if(response.code() != 401) {
                return Result.failure(Exception("Error al crear camapaña: ${response.message()}"))
            }

            val refresh = tokenManager.getRefreshToken().first()

            if(refresh == null) {
                return Result.failure(Exception("No hay token de refresco"))
            }

            val refreshSuccess = authRepository.refresh(refresh)

            if(refreshSuccess.isFailure) {
                return Result.failure(Exception("Refresh fallido"))
            }

            access = tokenManager.getAccessToken().first()
            response = campaignAPI.getCampaigns("Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteAll()
                campaignDAO.insertAll(response.body()!!)
                return Result.success("Camapañas insertadas correctamente")
            }

            return Result.failure(Exception("Fallo al cargar las camapañas"))

        }catch(e: Exception) {
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun updateCamaign(id: Int, title: String = "", description: String = "", imgUri: Uri = Uri.EMPTY, context: Context): Result<String> {
        try {
            val url = imgRepository.uploadImage(imgUri, context)
            val campaign = CreateCampaign(title,description,url)
            var access = tokenManager.getAccessToken().first()

            var response = campaignAPI.updateCampaign(id, campaign, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteById(id)
                campaignDAO.insert(response.body()!!)
                return Result.success("Campaña actualizada correctamente")
            }

            if(response.code() != 401) {
                return Result.failure(Exception("Error al actualizar camapaña: ${response.message()}"))
            }

            val refresh = tokenManager.getRefreshToken().first()

            if(refresh == null) {
                return Result.failure(Exception("No hay token de refresco"))
            }

            val refreshSuccess = authRepository.refresh(refresh)

            if(refreshSuccess.isFailure) {
                return Result.failure(Exception("Refresh fallido"))
            }

            access = tokenManager.getAccessToken().first()
            response = campaignAPI.updateCampaign(id, campaign, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteById(id)
                campaignDAO.insert(response.body()!!)
                return Result.success("Campaña actualizada correctamente")
            }

            return Result.failure(Exception("No se ha podido actualizar la campaña"))

        }catch (e: Exception) {
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun insertUser(inviteCode: String): Result<String> {
        try {
            var access = tokenManager.getAccessToken().first()
            var response = campaignAPI.addUser(inviteCode, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.insert(response.body()!!)
                return Result.success("Invitación aceptada")
            }

            if(response.code() != 401) {
                return Result.failure(Exception("Error al actualizar camapaña: ${response.message()}"))
            }

            val refresh = tokenManager.getRefreshToken().first()

            if(refresh == null) {
                return Result.failure(Exception("No hay token de refresco"))
            }

            val refreshSuccess = authRepository.refresh(refresh)

            if(refreshSuccess.isFailure) {
                return Result.failure(Exception("Refresh fallido"))
            }

            access = tokenManager.getAccessToken().first()
            response = campaignAPI.addUser(inviteCode, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.insert(response.body()!!)
                return Result.success("Invitación aceptada")
            }

            return Result.failure(Exception("No has podido entrar a la partida"))

        }catch (e: Exception) {
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun removeUser(id: Int): Result<String> {
        try {
            var access = tokenManager.getAccessToken().first()
            var response = campaignAPI.removeUser(id, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteById(id)
                return Result.success("Camapaña eliminada de forma exitosa")
            }

            if(response.code() != 401) {
                return Result.failure(Exception("Error al actualizar camapaña: ${response.message()}"))
            }

            val refresh = tokenManager.getRefreshToken().first()

            if(refresh == null) {
                return Result.failure(Exception("No hay token de refresco"))
            }

            val refreshSuccess = authRepository.refresh(refresh)

            if(refreshSuccess.isFailure) {
                return Result.failure(Exception("Refresh fallido"))
            }

            access = tokenManager.getAccessToken().first()
            response = campaignAPI.removeUser(id, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteById(id)
                return Result.success("Camapaña eliminada de forma exitosa")
            }

            return Result.failure(Exception("No has salirte de la partida"))

        }catch (e: Exception) {
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun removeCampaign(id: Int): Result<String> {
        try {
            var access = tokenManager.getAccessToken().first()
            var response = campaignAPI.deleteCampaign(id, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteById(id)
                return Result.success("Camapaña eliminada de forma exitosa")
            }

            if(response.code() != 401) {
                return Result.failure(Exception("Error al actualizar camapaña: ${response.message()}"))
            }

            val refresh = tokenManager.getRefreshToken().first()

            if(refresh == null) {
                return Result.failure(Exception("No hay token de refresco"))
            }

            val refreshSuccess = authRepository.refresh(refresh)

            if(refreshSuccess.isFailure) {
                return Result.failure(Exception("Refresh fallido"))
            }

            access = tokenManager.getAccessToken().first()
            response = campaignAPI.deleteCampaign(id, "Bearer $access")

            if(response.isSuccessful) {
                campaignDAO.deleteById(id)
                return Result.success("Camapaña eliminada de forma exitosa")
            }

            return Result.failure(Exception("La camapaña no se ha podido eliminar"))

        }catch (e: Exception) {
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

}