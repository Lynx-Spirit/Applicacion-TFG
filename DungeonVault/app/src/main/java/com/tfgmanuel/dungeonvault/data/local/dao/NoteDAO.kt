package com.tfgmanuel.dungeonvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tfgmanuel.dungeonvault.data.model.Note

/**
 * DAO para acceder a la tabla de notas en la base de datos local.
 */
@Dao
interface NoteDAO {
    /**
     * Inserta una nueva nota en la base de datos
     *
     * @param note Nota que se quisere insertar en la base de datos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    /**
     * Inserta una lista d enotas en la base de datos.
     *
     * @param notes Lista de notas a insertar.
     */
    @Transaction
    suspend fun insertAll(notes: List<Note>) {
        for (note in notes) {
            insert(note)
        }
    }

    /**
     * Obtiene todas las notas de la campaña
     *
     * @param campaignID Identificador de la campaña la cual se quiere obtener todas las notas.
     *
     * @return Listado de todas las notas de la campaña.
     */
    @Query("SELECT * FROM Note WHERE campaign_id = :campaignID")
    suspend fun getCampaignNotes(campaignID: Int): List<Note>

    /**
     * Actualización de la nota.
     *
     * @param note Nota actualizada
     */
    @Update
    suspend fun updateNote(note: Note)

    /**
     * Elimina nota seleccionada.
     *
     * @param id Identificador de la nota que se quiere eliminar.
     */
    @Query("DELETE FROM Note WHERE id = :id")
    suspend fun deleteNote(id: Int)

    /**
     * Elimina todas las notas de la base de datos.
     */
    @Query("DELETE FROM Note")
    suspend fun deleteAllNotes()
}