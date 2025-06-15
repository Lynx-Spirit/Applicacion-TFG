package com.tfgmanuel.dungeonvault.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "http://192.168.1.169:8000/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    /**
     * Proporciona una instancia de Retrofit configurada con la URL base.
     *
     * @return Instancia de [Retrofit]
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Proporciona una instancia de [AuthAPI] generada por Retrofit.
     *
     * @param retrofit Instancia de retrofit
     *
     * @return Instancia de [AuthAPI]
     */
    @Provides
    @Singleton
    fun provideAuthAPI(retrofit: Retrofit): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    /**
     * Proporciona una instancia de [CampaignAPI] generada por Retrofit.
     *
     * @param retrofit Instancia de retrofit
     *
     * @return Instancia de [CampaignAPI]
     */
    @Provides
    @Singleton
    fun provideCampaignAPI(retrofit: Retrofit): CampaignAPI {
        return retrofit.create(CampaignAPI::class.java)
    }

    /**
     * Proporciona una instancia de [FilesAPI] generada por Retrofit.
     *
     * @param retrofit Instancia de retrofit
     *
     * @return Instancia de [FilesAPI]
     */
    @Provides
    @Singleton
    fun provideFileAPI(retrofit: Retrofit): FilesAPI {
        return retrofit.create(FilesAPI::class.java)
    }

    /**
     * Proporciona una instancia de [NotesAPI] generada por Retrofit.
     *
     * @param retrofit Instancia de retrofit
     *
     * @return Instancia de [NotesAPI]
     */
    @Provides
    @Singleton
    fun provideNoteAPI(retrofit: Retrofit): NotesAPI {
        return retrofit.create(NotesAPI::class.java)
    }

    /**
     * Proporciona una instancia de [CharactersAPI] generada por Retrofit.
     *
     * @param retrofit Instancia de retrofit
     *
     * @return Instancia de [CharactersAPI]
     */
    @Provides
    @Singleton
    fun provideCharactersAPI(retrofit: Retrofit): CharactersAPI {
        return retrofit.create(CharactersAPI::class.java)
    }
}