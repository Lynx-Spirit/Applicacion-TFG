package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.ImgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Interfaz que define la llamada a la API relacionada con la subida de imágenes.
 * Utiliza Retrofit para mapear las peticiones HTTP.
 */
interface ImgAPI {
    /**
     * Sube una imagen al servidor.
     *
     * @param image Imagen a subir, enviada como una parte de un formulario multipart.
     * @return [Response] que contiene un [ImgResponse] con los datos de la imagen subida (por ejemplo, URL).
     */
    @Multipart
    @POST("images/upload")
    suspend fun uploadImg(
        @Part image: MultipartBody.Part
    ): Response<ImgResponse>
}