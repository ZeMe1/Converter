package kz.zeme.converter.core.observable.extensions

import kotlinx.coroutines.delay
import kz.zeme.converter.core.domain.Response
import kz.zeme.converter.core.error.UnexpectedError
import kz.zeme.converter.core.observable.ViewState

suspend fun <T : Any> ViewState<T>.getViewState(
    response: Response<T>,
): ViewState<T> = getViewState(
    response = response,
    transform = { it }
)

suspend fun <X : Any, Y : Any> ViewState<X>.getViewState(
    response: Response<Y>,
    transform: suspend (Y) -> X
): ViewState<X> {
    val remainingMs = (this as? ViewState.Loading)?.remainingMs ?: 0L
    delay(remainingMs)

    return when (response) {
        is Response.Failure -> getErrorState(response)
        is Response.Success -> try {
            ViewState.Data(transform(response.data))
        } catch (ex: Exception) {
            ViewState.Error(UnexpectedError(underlying = ex), data = data)
        }
    }
}

fun <T : Any, F : Any> ViewState<T>.getErrorState(
    response: Response.Failure<F>
): ViewState.Error<T> = ViewState.Error(
    error = response.data,
    data = data
)

fun <T : Any> ViewState<T>.getLoadingState(
    data: T? = this.data,
    loaderDurationMs: Long = ViewState.LOADER_DEFAULT_DURATION_MS,
): ViewState.Loading<T> = data
    ?.let { ViewState.Loading.ProgressBar(data = it, durationMs = loaderDurationMs) }
    ?: ViewState.Loading.Skeleton(durationMs = loaderDurationMs)

fun <X : Any, Y : Any> ViewState<X>.mapState(
    transform: (X) -> Y
): ViewState<Y> = when (this) {
    is ViewState.Data -> ViewState.Data(transform(this.data))
    is ViewState.Error -> ViewState.Error(this.error, this.data?.let(transform))
    is ViewState.Loading.ProgressBar -> ViewState.Loading.ProgressBar(
        data = this.data?.let(transform),
        durationMs = this.durationMs
    )
    is ViewState.Loading.Skeleton -> ViewState.Loading.Skeleton(
        data = this.data?.let(transform),
        durationMs = this.durationMs
    )
}
