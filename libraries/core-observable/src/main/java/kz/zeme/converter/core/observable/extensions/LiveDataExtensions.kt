package kz.zeme.converter.core.observable.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.zeme.converter.core.domain.Response
import kz.zeme.converter.core.error.UnexpectedError
import kz.zeme.converter.core.observable.Event
import kz.zeme.converter.core.observable.ViewState

fun <T : Any> MutableLiveData<ViewState<T>>.update(block: T.() -> T) {
    val update = (value?.data ?: return).block()
    value = ViewState.Data(update)
}

fun <T : Any> MutableLiveData<ViewState<T>>.postUpdate(block: T.() -> T) {
    val update = (value?.data ?: return).block()
    postValue(ViewState.Data(update))
}

fun <T : Any> MutableLiveData<ViewState<T>>.getLoadingState(
    data: T? = value?.data,
    loaderDurationMs: Long = ViewState.LOADER_DEFAULT_DURATION_MS,
): ViewState.Loading<T> = value?.getLoadingState(data, loaderDurationMs)
    ?: ViewState.Loading.Skeleton(durationMs = loaderDurationMs)

suspend fun <T : Any> MutableLiveData<ViewState<T>>.getResponse(
    response: Response<T>
) = getResponse(
    response = response,
    transform = { it }
)

suspend fun <X : Any, Y : Any> MutableLiveData<ViewState<X>>.getResponse(
    response: Response<Y>,
    transform: suspend (Y) -> X
) {
    value = value?.getViewState(response, transform)
        ?: when (response) {
            is Response.Failure -> ViewState.Error(response.data)
            is Response.Success -> try {
                ViewState.Data(transform(response.data))
            } catch (ex: Exception) {
                ViewState.Error(UnexpectedError(underlying = ex))
            }
        }
}

fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    onUpdate: (T) -> Unit
) {
    observe(owner) { event ->
        event?.getContentIfNotHandled()?.let(onUpdate)
    }
}
