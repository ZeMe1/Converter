package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class ConnectionError(
    override val code: ErrorCode = DefaultErrorCode.CONNECTION,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
