package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class UnexpectedError(
    override val code: ErrorCode = DefaultErrorCode.UNKNOWN,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
