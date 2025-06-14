package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.APIResponse
import com.tfgmanuel.dungeonvault.data.model.FileResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Interfaz que define la llamada a la API relacionada con la subida de imágenes.
 * Utiliza Retrofit para mapear las peticiones HTTP.
 */
interface FilesAPI {
    /**
     * Obtención de los ficheros desde la API
     *
     */
    @GET("files/{fileName}")
    suspend fun getFile(
        @Path("fileName") id: String
    ): Response<ResponseBody>

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

    /**
     * Actualiza un fichero del servidor.
     *
     * @param id Nombre del fichero que se quiere actualizar.
     * @param file Fichero que contiene toda la información actualizada.
     *
     * @return [APIResponse] que contiene el resultado de la ejecución.
     */
    @Multipart
    @PUT("files/{id}/update")
    suspend fun updateFile(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<APIResponse>
}