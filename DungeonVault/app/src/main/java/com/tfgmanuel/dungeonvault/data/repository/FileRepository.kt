package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import android.net.Uri
import com.tfgmanuel.dungeonvault.data.remote.FilesAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FileRepository @Inject constructor(
    private val filesAPI: FilesAPI,
) {
    /**
     * Subida de una imagen seleccionada al servidor.
     *
     * @param imageUri Referencia de la imagen que quiere subirse.
     *
     * @return [String] que contiene el nombre del archivo de imagen en el servidor.
     * Si no se proporciona una URI, devuelve una cadena vacía.
     */
    suspend fun uploadImage(imageUri: Uri, context: Context): String {
        if (imageUri == Uri.EMPTY) {
            return ""
        } else {
            val imageFile = getFileFromUri(imageUri, context)
            val imagePart = prepareImageFile(imageFile)

            val result = filesAPI.uploadFile(imagePart)

            if (result.isSuccessful) {
                val imgResponse = result.body()!!
                return imgResponse.filename
            } else {
                return ""
            }
        }
    }

    /**
     * Convierte un URI en un archivo físico temporal almacenado en el directorio de caché de la app.
     *
     * @param uri El URI del recurso que se desea convertir en archivo.
     * @param context El contexto necesario para acceder al content resolver y al directorio de caché.
     *
     * @return Un archivo temporal creado a partir del contenido del URI.
     */
    private fun getFileFromUri(uri: Uri, context: Context): File {
        val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
            outputStream.write(buffer, 0, length)
        }

        inputStream?.close()
        outputStream.close()

        return file
    }

    /**
     * Prepara un archivo de imagen para ser enviado en una solicitud HTTP como parte de un formulario multipart.
     *
     * @param file El archivo de imagen que se desea enviar.
     *
     * @return Un objeto MultipartBody.Part listo para ser incluido en una solicitud HTTP.
     */
    private fun prepareImageFile(file: File): MultipartBody.Part {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }
}