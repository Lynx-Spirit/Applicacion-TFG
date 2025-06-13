package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.FileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Interfaz que define la llamada a la API relacionada con la subida de imágenes.
 * Utiliza Retrofit para mapear las peticiones HTTP.
 */
interface FilesAPI {
    /**
     * Sube una imagen al servidor.
     *
     * @param file Imagen a subir, enviada como una parte de un formulario multipart.
     * @return [Response] que contiene un [FileResponse] con los datos del fichero subido.
     */
    @Multipart
    @POST("files/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): Response<FileResponse>
}