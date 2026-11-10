package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class NotFoundError(
    override val code: ErrorCode = DefaultErrorCode.NOT_FOUND,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
