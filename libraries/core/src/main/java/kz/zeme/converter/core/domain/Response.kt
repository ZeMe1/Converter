package kz.zeme.converter.core.domain

import kz.zeme.converter.core.error.BaseError

sealed class Response<T : Any> {
    data class Success<T : Any>(val data: T) : Response<T>()
    data class Failure<T : Any>(val data: BaseError) : Response<T>()
}
