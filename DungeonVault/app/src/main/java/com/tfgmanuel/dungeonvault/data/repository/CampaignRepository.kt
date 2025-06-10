package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import android.net.Uri
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.local.dao.UserDAO
import com.tfgmanuel.dungeonvault.data.model.CampaignUserCrossRef
import com.tfgmanuel.dungeonvault.data.model.CreateCampaign
import com.tfgmanuel.dungeonvault.data.model.KickInformation
import com.tfgmanuel.dungeonvault.data.remote.CampaignAPI
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CampaignRepository @Inject constructor(
    private val imgRepository: ImgRepository,
    private val campaignAPI: CampaignAPI,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val campaignDAO: CampaignDAO,
    private val userDAO: UserDAO
) {

    /**
     * Creación de una nueva campaña por parte de un usuario autenticado.
     *
     * @param title Titulo de la nueva campaña.
     * @param description Descripción de la nueva campoña.
     * @param imgUri Identificador de la imagen de la campaña.
     * @param context Contexto de la aplicación.
     */
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

    /**
     * Primer intento de creación de la campaña.
     * En caso de que devuelva la API un 401, retorna null para poder refrescar los tokens.
     *
     * @param campaign Objeto
     */
    private suspend fun firstAttemptCreate(campaign: CreateCampaign): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.createCampaign(campaign, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            userDAO.insertUserCampaignCrossRef(
                CampaignUserCrossRef(
                    response.body()!!.id,
                    tokenManager.getUserID().first()!!
                )
            )
            return Result.success("Campaña insertada con éxito")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de creación de la campaña tras haber refrescado los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptCreate(campaign: CreateCampaign): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.createCampaign(campaign, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            userDAO.insertUserCampaignCrossRef(
                CampaignUserCrossRef(
                    response.body()!!.id,
                    tokenManager.getUserID().first()!!
                )
            )
            return Result.success("Campaña insertada tras refresh")
        } else {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }
    }

    /**
     * Obtención de los datos específicos de una campaña.
     *
     * @param id Identificador de la campaña que se quiere obtener la información.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun getCampaign(id: Int): Result<String> {
        return firstAttemptGet(id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptGet(id) }
    }

    /**
     * Primer intento de obtención la la informacíon de una campaña específica
     * En caso de que devuelva la API un 401, retorna null para poder refrescar los tokens.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptGet(id: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.getCampaign(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            return Result.success("Camapaña insertada correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de obtener la información de una campaña específica del usuario tras haber refrescado los tokens.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptGet(id: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.getCampaign(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            return Result.success("Camapaña insertada correctamente")
        }

        return Result.failure(Exception("Fallo al cargar la camapaña"))
    }

    /**
     * Obtención de todas las campañas en las que esté el usuario como miembro
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun getAllCampaigns(): Result<String> {
        return firstAttemptGet()
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptGet() }
    }

    /**
     * Primer intento de obtención de todas las campañas del usuario.
     * En caso de que devuelva la API un 401, retorna null para poder refrescar los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Segundo y último intento de obtención de todas las campañas del usuario tras haber refrescado los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Obtención de todos los usuarios que sean miembros de una campaña.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun getUsers(
        id: Int
    ): Result<String> {
        return firstAttemptGetUsers(id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptGetUsers(id) }
    }

    /**
     * Primer intento de obtención de la información del usuario.
     * En caso de que devuelva la API 401, retornará null para poder refrescar los tokens.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptGetUsers(
        id: Int
    ): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.getMembers(id, "Bearer $access")

        if (response.isSuccessful) {
            userDAO.insertUsers(response.body()!!)
            userDAO.insertCrossRefs(response.body()!!,id)
            return Result.success("Usuarios obtenidos correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de obtención de los usuarios miembors de la campaña tras haber refrescado los tokens del usuario.
     *
     * @param id Identificador de la campaña
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución
     */
    private suspend fun secondAttemptGetUsers(
        id: Int
    ): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.getMembers(id, "Bearer $access")

        if (response.isSuccessful) {
            userDAO.insertUsers(response.body()!!)
            userDAO.insertCrossRefs(response.body()!!,id)
            return Result.success("Usuarios obtenidos correctamente")
        } else {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }
    }

    /**
     * Actualización de la información de la campaña.
     *
     * @param id  Identificador de la campaña.
     * @param title Nuevo título de la campaña.
     * @param description Nueva descripción de la campaña.
     * @param originalFileName Nombre original de la imagen asociada a la campaña.
     * @param imgUri URI de la nueva imagen.
     * @param context Contexto de la aplicación.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Primer intento de actualización de la campaña.
     *
     * @param id Identificador de la campaña a actualizar.
     * @param campaign Campaña con toda la información actualizada.
     *
     * En caso de devolver la API 401, retorna null para poder refrescar los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Segundo y último intento para actualizar los datos de la campaña con los tokens refrescados.
     *
     * @param id Identificador de la campaña a actualizar.
     * @param campaign Campaña con toda la información actualizada.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Inserta al usuario como miembro dentro de una campaña.
     *
     * @param inviteCode Código de invitación de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun insertUser(inviteCode: String): Result<String> {
        return firstAttemptInsertUser(inviteCode = inviteCode)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptInsertUser(inviteCode = inviteCode) }
    }

    /**
     * Primer intento de insertar al usuario como miembro dentro de una campaña
     * En caso de que devuelva la API 401, retorna null para poder refrescar los tokens.
     *
     * @param inviteCode Código de invitación de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptInsertUser(inviteCode: String): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.addUser(inviteCode, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            userDAO.insertUserCampaignCrossRef(
                CampaignUserCrossRef(
                    response.body()!!.id,
                    tokenManager.getUserID().first()!!
                )
            )
            return Result.success("Invitación aceptada")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de insertar al usuario como miembro dentro de una campaña tras haber refrescado los tokens.
     *
     * @param inviteCode Código de invitación de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptInsertUser(inviteCode: String): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.addUser(inviteCode, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.insert(response.body()!!)
            userDAO.insertUserCampaignCrossRef(
                CampaignUserCrossRef(
                    response.body()!!.id,
                    tokenManager.getUserID().first()!!
                )
            )
            return Result.success("Invitación aceptada")
        }

        return Result.failure(Exception("No has podido entrar a la partida"))
    }

    /**
     * Elimina al usuario de la campaña.
     *
     * @param id Identificador de la campaña
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun removeUser(id: Int): Result<String> {
        return firstAttemptRemoveUser(id = id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptRemoveUser(id = id) }
    }

    /**
     * Primer intento de eliminar al usuario de la campaña.
     * En caso de que devuelva la API 401, retornará null para poder refrescarse los tokens.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun firstAttemptRemoveUser(id: Int): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.removeUser(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            campaignDAO.kickUserFromCampaign(id, tokenManager.getUserID().first()!!)
            return Result.success(response.body()!!.message)
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento para eliminar el usuario de la campaña tras haber refrescado los tokens.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptRemoveUser(id: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.removeUser(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            campaignDAO.kickUserFromCampaign(id, tokenManager.getUserID().first()!!)
            return Result.success(response.body()!!.message)
        }

        return Result.failure(Exception("No has podido salirte de la partida"))
    }

    /**
     * Elimina la campaña.
     *
     * @param id Identificador de la campaña
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun removeCampaign(id: Int): Result<String> {
        return firstAttemptRemoveCampaign(id = id)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptRemoveCampaign(id = id) }
    }

    /**
     * Primer intento de eliminar la campaña.
     * En caso de que devuelva la API 401, retornará null para poder refrescarse los tokens.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Segundo y último intento para eliminar la campaña tras haber refrescado los tokens.
     *
     * @param id Identificador de la campaña.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptRemoveCampaign(id: Int): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = campaignAPI.deleteCampaign(id, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.deleteById(id)
            return Result.success("Camapaña eliminada de forma exitosa")
        }

        return Result.failure(Exception("La camapaña no se ha podido eliminar"))
    }

    /**
     * Echa a un usuario determinado de la campaña.
     *
     * @param id Identificador de la campaña.
     * @param userId Identificador del usuario.
     *
     * @return Devuelve un [Result] con un [String] indicando el resultado de la ejecución.
     */
    suspend fun kickUser(
        id: Int,
        userId: Int
    ): Result<String> {
        val kickInfo = KickInformation(userId, id)
        return firstAttemptKick(kickInfo)
            ?: authRepository.refreshTokens()
                .flatMap { secondAttemptKick(kickInfo) }
    }

    /**
     * Primer intento de echar a un usuario de la campaña.
     * En caso de que la API devuelva 401, se retornará null para poder refrescar los token.
     *
     * @param kickInfo Objeto que contiene la información necesaria paar poder echar al usuario.
     *
     * @return Devuelve un [Result] con un [String] indicando el resultado de la ejecución.
     */
    private suspend fun firstAttemptKick(
        kickInfo: KickInformation
    ): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response =
            campaignAPI.kickUSer(id = kickInfo.id, kickInformation = kickInfo, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.kickUserFromCampaign(kickInfo.id, kickInfo.user)
            return Result.success("Usuario echado de forma correcta")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    /**
     * Segundo y último intento de echar a un usuario de la campaña una vez se han refrescado los tokens.
     *
     * @param kickInfo Objeto que contiene la información necesaria paar poder echar al usuario.
     *
     * @return Devuelve un [Result] con un [String] indicando el resultado de la ejecución.
     */
    private suspend fun secondAttemptKick(
        kickInfo: KickInformation
    ): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response =
            campaignAPI.kickUSer(id = kickInfo.id, kickInformation = kickInfo, "Bearer $access")

        if (response.isSuccessful) {
            campaignDAO.kickUserFromCampaign(kickInfo.id, kickInfo.user)
            return Result.success("Usuario echado de forma correcta")
        } else {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }
    }
}