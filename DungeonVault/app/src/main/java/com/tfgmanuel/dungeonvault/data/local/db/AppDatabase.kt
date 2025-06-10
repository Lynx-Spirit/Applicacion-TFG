package com.tfgmanuel.dungeonvault.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.local.dao.UserDAO
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.model.CampaignUserCrossRef

@Database(entities = [Campaign::class, User::class, CampaignUserCrossRef::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Proporciona el DAO para acceder a la tabla de campañas.
     */
    abstract fun campaignDAO(): CampaignDAO

    /**
     * Proporciona el DAO para acceder a la tabla de usuarios
     */
    abstract  fun userDAO(): UserDAO
}