package kz.zeme.converter.core.network.extensions

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kz.zeme.converter.core.domain.Response

fun <T : Any> Gson.mapFailure(
    response: retrofit2.Response<*>,
): Response.Failure<T> {
    val errorString = response.errorBody()?.string()
    val message = try {
        if (!errorString.isNullOrBlank()) {
            fromJson(errorString, ApiErrorResponse::class.java)?.message ?: errorString
        } else response.message()
    } catch (e: Exception) {
        errorString ?: response.message()
    }

    return Response.Failure(
        toBaseError(
            httpCode = response.code(),
            message = message,
            underlying = null,
        )
    )
}

class ApiErrorResponse {

    @SerializedName(value = "message", alternate = ["error", "description"])
    var message: String? = null
}
