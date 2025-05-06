package com.tfgmanuel.dungeonvault.data.repository

import org.json.JSONObject
import retrofit2.Response

fun <T> getErrorFromApi(response: Response<T>): String {
    val errorJson = JSONObject(
        response.errorBody()?.string() ?: "{\"detail\": \"Error desconocido\"}"
    )
    return errorJson.getString("detail")
}

inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )
}