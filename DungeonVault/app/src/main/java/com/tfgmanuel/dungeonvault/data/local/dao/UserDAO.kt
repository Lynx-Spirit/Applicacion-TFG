package com.tfgmanuel.dungeonvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tfgmanuel.dungeonvault.data.model.CampaignUserCrossRef
import com.tfgmanuel.dungeonvault.data.model.User

/**
 * DAO para acceder a la tabla de usuarios en la base de datos local.
 */
@Dao
interface UserDAO {

    /**
     * Inserta o actualiza un usuario en la base de datos.
     * Si el usuario ya existe (basado en su ID), se actualiza con los nuevos datos.
     *
     * @param user Usuario que se va a insertar o actualizar
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserCampaignCrossRef(crossRef: CampaignUserCrossRef)

    @Transaction
    suspend fun insertCrossRefs(users: List<User>, id: Int) {
        for (user in users) {
            val crossRef = CampaignUserCrossRef(id, user.id)
            insertUserCampaignCrossRef(crossRef)
        }
    }

    /**
     * Inserta o actualiza una lista de usuarios en la base de datos y los mete en la campaña correspondiente
     * Si el usuario ya existe (basado en su ID), se actualiza con los nuevos datos.
     *
     * @param users Lista de usuarios que se van a insertar o actualizar.
     * @param campaignId ID de la campaña en la que se quiere insertar el usuario.
     */
    @Transaction
    suspend fun insertUsers(users: List<User>) {
        for (user in users) {
            if(getUser(user.id) == null) {
                insertUser(user)
            } else {
                updateUser(user)
            }
        }
    }

    /**
     * Actualiza el usuario
     *
     * @param user Objeto de tipo usuario a actualizar
     */
    @Update
    suspend fun updateUser(user: User)

    /**
     * Obtiene un usuario concreto por su id.
     *
     * @param id Identificador del usuario
     */
    @Query("SELECT * FROM Users WHERE id = :id")
    suspend fun getUser(id: Int): User?

    /**
     * Elimina todos los usuarios de la base de datos.
     */
    @Query("DELETE FROM Users")
    suspend fun deleteAllUsers()
}