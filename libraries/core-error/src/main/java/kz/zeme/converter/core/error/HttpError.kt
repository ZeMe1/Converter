package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class HttpError(
    val httpCode: Int = 0,
    override val code: ErrorCode = DefaultErrorCode.HTTP,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
