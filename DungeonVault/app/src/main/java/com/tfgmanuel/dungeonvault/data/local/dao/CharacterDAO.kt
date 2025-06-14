package com.tfgmanuel.dungeonvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tfgmanuel.dungeonvault.data.model.Character

/**
 * DAO para acceder a la tabla de personajes en la base de datos local.
 */
@Dao
interface CharacterDAO {
    /**
     * Inserta un nuevo personaje en la base de datos
     *
     * @param character El personaje que se quiere insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)

    /**
     * Inserta una lista de personajes en la base de datos.
     *
     * @param characters Lista de personajes a insertar
     */
    @Transaction
    suspend fun insertAll(characters: List<Character>) {
        for (character in characters) {
            insert(character)
        }
    }

    /**
     * Obtiene todos los personajes de la campaña
     *
     * @param campaignID Identificador de la campaña la cual se quiere obtener los personajes.
     *
     * @return Listado de todos los personajes de la campaña.
     */
    @Query("SELECT * FROM Character WHERE campaign_id = :campaignID")
    suspend fun getCampaignCharacters(campaignID: Int): List<Character>

    /**
     * Actualización de los datos de un personaje
     *
     * @param character Personaje con los datos actualizados.
     */
    @Update
    suspend fun updateCharacter(character: Character)

    /**
     * Elimina el personaje pasado como parámetro.
     *
     * @param id Identificaodr del personaje que se quiere eliminar.
     */
    @Query("DELETE FROM Character WHERE id = :id")
    suspend fun deleteCharacter(id: Int)

    /**
     * Elimina todos los personajes de la base de datos.
     */
    @Query("DELETE FROM Character")
    suspend fun deleteAllCharacters()
}