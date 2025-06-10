package com.tfgmanuel.dungeonvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.User

/**
 * DAO para acceder a la tabla de campañas en la base de datos local.
 */
@Dao
interface CampaignDAO {

    /**
     * Inserta una nueva campaña en la base de datos.
     *
     * @param campaign  La campaña que se va a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(campaign: Campaign)

    /**
     * Inserta una lista de campañas en la base de datos.
     *
     * @param campaigns Lista de campañas a insertar.
     */
    @Transaction
    suspend fun insertAll(campaigns: List<Campaign>) {
        for (campaign in campaigns) {
            campaign.lastUpdated = System.currentTimeMillis()
            insert(campaign)
        }
    }

    /**
     * Obtiene todas las campañas en la base de datos.
     *
     * @return Listado de todas las campañas.
     */
    @Query("SELECT * FROM Campaigns")
    suspend fun getAllCampaigns(): List<Campaign>

    /**
     * Obtiene una campaña a partir de su identificador.
     *
     * @param id Identificador de la campaña que se quiere buscar.
     *
     * @return La campaña correspondiente al identificador, o 'null' en caso de no encontrarse.
     */
    @Query("SELECT * FROM Campaigns WHERE id = :id LIMIT 1")
    suspend fun getCampaignById(id: Int): Campaign?

    /**
     * Obtiene todos los usuarios de una campaña determinada.
     *
     * @param campaignId El ID de la campaña para la cual obtener los usuarios.
     * @return Una lista de usuarios que están en la campaña.
     */
    @Query("""
        SELECT * FROM Users
        INNER JOIN CampaignUserCrossRef ON Users.id = CampaignUserCrossRef.userId
        WHERE CampaignUserCrossRef.campaignId = :campaignId
    """)
    suspend fun getUsersByCampaign(campaignId: Int): List<User>

    /**
     * Elimina una campaña a partir de su identificador.
     *
     * @param id Identificador de la campaña a eliminar.
     */
    @Query("DELETE FROM Campaigns WHERE id = :id")
    suspend fun deleteById(id: Int)

    /**
     * Elimina todas las campañas de la base de datos.
     */
    @Query("DELETE FROM campaigns")
    suspend fun deleteAll()

    /**
    * Elimina la relación entre un usuario y una campaña.
    *
    * @param campaignId El ID de la campaña.
    * @param userId El ID del usuario que se va a eliminar de la campaña.
    */
    @Query("""
        DELETE FROM CampaignUserCrossRef
        WHERE campaignId = :campaignId AND userId = :userId
    """)
    suspend fun kickUserFromCampaign(campaignId: Int, userId: Int)
}