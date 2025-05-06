package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import android.net.Uri
import com.tfgmanuel.dungeonvault.data.remote.ImgAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImgRepository @Inject constructor(
    private val imgAPI: ImgAPI,
) {
    suspend fun uploadImage(imageUri: Uri, context: Context): String {
        if (imageUri == Uri.EMPTY) {
            return ""
        } else {
            val imageFile = getFileFromUri(imageUri, context)
            val imagePart = prepareImageFile(imageFile)

            val result = imgAPI.uploadImg(imagePart)

            if (result.isSuccessful) {
                val imgResponse = result.body()!!
                return imgResponse.filename
            } else {
                return ""
            }
        }
    }

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

    private fun prepareImageFile(file: File): MultipartBody.Part {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }
}