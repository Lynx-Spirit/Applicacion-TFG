package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.ImgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgAPI {
    @POST("images/upload")
    suspend fun uploadImg(
        @Part("file") image: MultipartBody.Part
    ):Response<ImgResponse>
}