package com.tfgmanuel.dungeonvault.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "http://172.24.0.1:8000/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthAPI(retrofit: Retrofit): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCampaignAPI(retrofit: Retrofit): CampaignAPI {
        return retrofit.create(CampaignAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideImgAPI(retrofit: Retrofit): ImgAPI {
        return retrofit.create(ImgAPI::class.java)
    }
}