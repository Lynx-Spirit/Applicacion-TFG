package com.tfgmanuel.dungeonvault.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.local.dao.CharacterDAO
import com.tfgmanuel.dungeonvault.data.local.dao.NoteDAO
import com.tfgmanuel.dungeonvault.data.local.dao.UserDAO
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.model.CampaignUserCrossRef
import com.tfgmanuel.dungeonvault.data.model.Character
import com.tfgmanuel.dungeonvault.data.model.Note

@Database(
    entities = [Campaign::class, User::class, CampaignUserCrossRef::class, Note::class, Character::class],
    version = 8
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Proporciona el DAO para acceder a la tabla de campañas.
     */
    abstract fun campaignDAO(): CampaignDAO

    /**
     * Proporciona el DAO para acceder a la tabla de usuarios.
     */
    abstract fun userDAO(): UserDAO

    /**
     * Proporciona el DAO para acceder a la tabla de notas.
     */
    abstract fun noteDAO(): NoteDAO

    /**
     * Proporciona el DAO para acceder a la tabla de personajes.
     */
    abstract fun characterDAO(): CharacterDAO
}