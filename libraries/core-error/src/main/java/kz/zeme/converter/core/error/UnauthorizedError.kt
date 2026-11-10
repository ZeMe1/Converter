package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class UnauthorizedError(
    override val code: ErrorCode = DefaultErrorCode.UNAUTHORIZED,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
