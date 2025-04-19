package com.tfgmanuel.dungeonvault.data.local.db

import android.content.Context
import androidx.room.Room
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dungeonvault-db"
        ).build()
    }

    @Provides
    fun provideCampaignDao(database: AppDatabase): CampaignDAO {
        return database.campaignDAO()
    }
}