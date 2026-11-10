package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidationError(
    override val code: ErrorCode = DefaultErrorCode.VALIDATION,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
