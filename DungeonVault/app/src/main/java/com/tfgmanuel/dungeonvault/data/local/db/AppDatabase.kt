package com.tfgmanuel.dungeonvault.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.model.Campaign

@Database(entities = [Campaign:: class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun campaignDAO(): CampaignDAO
}