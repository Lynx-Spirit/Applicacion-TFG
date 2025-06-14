package com.tfgmanuel.dungeonvault.data.local.db

import android.content.Context
import androidx.room.Room
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.local.dao.CharacterDAO
import com.tfgmanuel.dungeonvault.data.local.dao.NoteDAO
import com.tfgmanuel.dungeonvault.data.local.dao.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Proporciona una instancia singleton de la base de datos, construida con Room.
     *
     * @param context Contexto de la aplicación, utilizado para construir la base de datos.
     * @return Una instancia de [AppDatabase].
     *
     * En caso de no proporcionarse una estrategia de migración, se eliminan todos los datos existentes.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dungeonvault-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Proporciona una instancia de [CampaignDAO] para acceder a la tabla de campañas.
     *
     * @param database La base de datos a partir de la cual se obtiene el DAO.
     * @return Una instancia de [CampaignDAO].
     */
    @Provides
    fun provideCampaignDao(database: AppDatabase): CampaignDAO {
        return database.campaignDAO()
    }

    /**
     * Proporciona una instancia de [UserDAO] para acceder a la tabla de usuarios
     *
     * @param database La base de datos a partir de la cual se obtiene el DAO.
     * @return Una instancia de [UserDAO].
     */
    @Provides
    fun provideUserDAO(database: AppDatabase): UserDAO {
        return database.userDAO()
    }

    /**
     * Proporciona una instancia de [NoteDAO] para acceder a la tabla de notas
     *
     * @param database La base de datos a partir de la cual se obtiene el DAO.
     * @return Una instancia de [NoteDAO].
     */
    @Provides
    fun provideNoteDAO(database: AppDatabase): NoteDAO {
        return database.noteDAO()
    }

    /**
     * Proporciona una instancia de [CharacterDAO] para acceder a la tabla de personajes
     *
     * @param database La base de datos a partir de la cual se obtiene el DAO.
     * @return Una instancia de [CharacterDAO].
     */
    @Provides
    fun provideCharacterDAO(database: AppDatabase): CharacterDAO {
        return database.characterDAO()
    }
}