package com.tfgmanuel.dungeonvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tfgmanuel.dungeonvault.data.model.Campaign

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
    @Insert
    suspend fun insert(campaign: Campaign)

    /**
     * Inserta una lista de campañas en la base de datos.
     *
     * @param campaigns Lista de campañas a insertar.
     */
    @Insert
    suspend fun insertAll(campaigns: List<Campaign>)

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
}