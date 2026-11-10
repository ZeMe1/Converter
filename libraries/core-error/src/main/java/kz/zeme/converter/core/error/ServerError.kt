package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class ServerError(
    val serverCode: String? = null,
    override val code: ErrorCode = DefaultErrorCode.SERVER,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
