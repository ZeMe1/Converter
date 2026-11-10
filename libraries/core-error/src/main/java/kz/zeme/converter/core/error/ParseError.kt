package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class ParseError(
    override val code: ErrorCode = DefaultErrorCode.PARSE,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)
