package kz.zeme.converter.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
enum class DefaultErrorCode : BaseError.ErrorCode {
    UNKNOWN,
    CONNECTION,
    HTTP,
    SERVER,
    UNAUTHORIZED,
    NOT_FOUND,
    PARSE,
    SSL,
    VALIDATION,
}
