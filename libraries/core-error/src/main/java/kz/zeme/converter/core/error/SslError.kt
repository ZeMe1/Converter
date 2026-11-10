package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class SslError(
    override val code: ErrorCode = DefaultErrorCode.SSL,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
