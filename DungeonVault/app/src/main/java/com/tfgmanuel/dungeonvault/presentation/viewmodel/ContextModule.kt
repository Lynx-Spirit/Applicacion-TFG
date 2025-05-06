package com.tfgmanuel.dungeonvault.presentation.viewmodel

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object ContextModule {

    @Provides
    fun provideContextProvider(application: Application): ContextProvider {
        return DefaultContextProvider(application)
    }
}

class DefaultContextProvider @Inject constructor(
    private val application: Application
) : ContextProvider {
    override fun getContext(): Context = application
}