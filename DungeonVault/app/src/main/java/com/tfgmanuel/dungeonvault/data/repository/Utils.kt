package com.tfgmanuel.dungeonvault.data.repository

import org.json.JSONObject
import retrofit2.Response

/**
 * Extrae el mensaje de eror de una respuesta HTTP fallida.
 * Intenta parsear el cuerpo del error como un objeto JSON y obtener el campo "detail".
 * Si no es posible, devuelve un mensaje de error genérico.
 *
 * @param response La respuesta HTTP recibida.
 * @return Mensaje de error extraído del campo "detail".
 */
fun <T> getErrorFromApi(response: Response<T>): String {
    val errorJson = JSONObject(
        response.errorBody()?.string() ?: "{\"detail\": \"Error desconocido\"}"
    )
    return errorJson.getString("detail")
}

/**
 * Transforma un [Result] exitoso aplicando una funcíon que devuelve otro [Result].
 *
 * Si el 'Result' es exitoso, se aplica la transformación.
 * Si es un error, simplemente se propaga el mismo error sin aplicar la función.
 *
 * @param transform Funcíon que transforma un valor exitoso en otro [Result].
 * @return Un nuevo [Result], ya sea transformado o con el mismo error original.
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )
}