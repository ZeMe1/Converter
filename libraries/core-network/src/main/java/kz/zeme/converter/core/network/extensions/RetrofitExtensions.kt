package kz.zeme.converter.core.network.extensions

import kz.zeme.converter.core.domain.Response
import retrofit2.HttpException
import retrofit2.Response as RetroResponse

suspend fun <Api : Any, Domain : Any> RetroResponse<Api>.map(
    mapSuccess: suspend (Api) -> Domain,
    mapSuccessNullBody: (suspend (Api?) -> Response<Domain>)? = null,
    mapFailure: (RetroResponse<Api>) -> Response<Domain> = {
        Response.Failure(
            toBaseError(
                httpCode = code(),
                message = message(),
                underlying = HttpException(this),
            )
        )
    },
): Response<Domain> {
    if (isSuccessful) {
        val body = body()
        return when {
            body != null -> Response.Success(mapSuccess(body))
            mapSuccessNullBody != null -> mapSuccessNullBody(body)
            else -> mapFailure(this)
        }
    }
    return mapFailure(this)
}

suspend fun <Api : Any, Domain : Any> tryMapSuspended(
    request: suspend () -> RetroResponse<Api>,
    mapSuccess: suspend (Api) -> Domain,
    mapSuccessNullBody: (suspend (Api?) -> Response<Domain>)? = null,
    mapFailure: (RetroResponse<Api>) -> Response<Domain>,
    mapException: (Exception) -> Response<Domain> = { ex ->
        Response.Failure(ex.toBaseError())
    },
): Response<Domain> = try {
    request().map(
        mapSuccess = mapSuccess,
        mapSuccessNullBody = mapSuccessNullBody,
        mapFailure = mapFailure,
    )
} catch (ex: Exception) {
    mapException(ex)
}

suspend fun <Domain : Any> trySuspended(
    request: suspend () -> Domain,
    mapException: (Exception) -> Response<Domain> = { ex ->
        Response.Failure(ex.toBaseError())
    },
): Response<Domain> = try {
    Response.Success(request())
} catch (ex: Exception) {
    mapException(ex)
}
