package kz.zeme.converter.core.network.extensions

import com.google.gson.JsonParseException
import kz.zeme.converter.core.error.BaseError
import kz.zeme.converter.core.error.ConnectionError
import kz.zeme.converter.core.error.HttpError
import kz.zeme.converter.core.error.ParseError
import kz.zeme.converter.core.error.ServerError
import kz.zeme.converter.core.error.UnauthorizedError
import kz.zeme.converter.core.error.UnexpectedError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toBaseError(): BaseError = when (this) {
    is BaseError -> this
    is HttpException -> toBaseError(
        httpCode = code(),
        message = message(),
        underlying = this,
    )
    is JsonParseException -> ParseError(
        underlying = this,
        message = message,
    )
    is IOException -> ConnectionError(
        underlying = this,
        message = message,
    )
    else -> UnexpectedError(
        message = message,
        underlying = this,
    )
}

fun toBaseError(
    httpCode: Int,
    message: String?,
    underlying: Throwable?,
): BaseError = when (httpCode) {
    401 -> UnauthorizedError(
        message = message,
        underlying = underlying,
    )
    in 500..599 -> ServerError(
        serverCode = httpCode.toString(),
        message = message,
        underlying = underlying,
    )
    else -> HttpError(
        httpCode = httpCode,
        message = message,
        underlying = underlying,
    )
}
