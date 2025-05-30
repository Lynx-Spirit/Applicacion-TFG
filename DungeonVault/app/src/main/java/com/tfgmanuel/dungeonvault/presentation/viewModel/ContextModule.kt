package com.tfgmanuel.dungeonvault.presentation.viewModel

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

/**
 * Módulo de Dagger-Hilt responsable de proveer instancias de `ContextProvider` para su inyección
 * en toda la aplicación.
 *
 * Este módulo se instala en el `SingletonComponent`, lo que significa que las dependencias
 * proporcionadas tendrán un ciclo de vida que coincide con el ciclo de vida de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object ContextModule {

    /**
     * Proveedor de `ContextProvider`. Proporciona una instancia de `DefaultContextProvider`
     * utilizando el contexto de la aplicación.
     *
     * @param application Contexto de la aplicación que se proporciona desde Dagger.
     * @return Una instancia de `ContextProvider`.
     */
    @Provides
    fun provideContextProvider(application: Application): ContextProvider {
        return DefaultContextProvider(application)
    }
}
/**
 * Implementación de la interfaz `ContextProvider`. Esta clase proporciona el contexto
 * de la aplicación a través de su método `getContext()`.
 *
 * @param application La instancia de la aplicación que se utiliza para obtener el contexto.
 */
class DefaultContextProvider @Inject constructor(
    private val application: Application
) : ContextProvider {
    /**
     * Devuelve el contexto de la aplicación.
     *
     * @return El contexto de la aplicación.
     */
    override fun getContext(): Context = application
}