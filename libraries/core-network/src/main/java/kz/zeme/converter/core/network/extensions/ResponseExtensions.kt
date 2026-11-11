package kz.zeme.converter.core.network.extensions

import kz.zeme.converter.core.domain.Response
import kz.zeme.converter.core.observable.ViewState

fun <T : Any> Response<T>.extractValueOrNull(): T? = when (this) {
    is Response.Failure -> null
    is Response.Success -> data
}

fun <T : Any> Response<T>.extractValueOrDefault(
    default: T,
): T = when (this) {
    is Response.Failure -> default
    is Response.Success -> data
}

fun <T : Any> Response<T>.getViewState(): ViewState<T> = when (this) {
    is Response.Failure -> ViewState.Error(data)
    is Response.Success -> ViewState.Data(data)
}

fun <T : Any, X : Any> Response<T>.getViewState(
    transform: (T) -> X,
): ViewState<X> = when (this) {
    is Response.Failure -> ViewState.Error(data)
    is Response.Success -> ViewState.Data(transform(data))
}
