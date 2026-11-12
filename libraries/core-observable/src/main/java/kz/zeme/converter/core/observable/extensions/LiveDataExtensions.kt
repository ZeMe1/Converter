package kz.zeme.converter.core.observable.extensions

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import kotlinx.coroutines.delay
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

fun <T : Any> MutableLiveData<T>.updateValue(block: (T?) -> T?) {
    value = block(value)
}

fun <T : Any> MutableLiveData<T>.postValue(block: (T?) -> T?) {
    postValue(block(value))
}

@MainThread
fun <T : Any> LiveData<ViewState<T>>.getLoadingState(
    data: T? = value?.data,
    loaderDurationMs: Long = ViewState.LOADER_DEFAULT_DURATION_MS,
): ViewState.Loading<T> = data
    ?.let { ViewState.Loading.ProgressBar(data = it, durationMs = loaderDurationMs) }
    ?: ViewState.Loading.Skeleton(durationMs = loaderDurationMs)

@MainThread
suspend fun <T : Any> LiveData<ViewState<T>>.getViewState(
    response: Response<T>,
): ViewState<T> = getViewState(
    response = response,
    transform = { it },
)

@MainThread
suspend fun <X : Any, Y : Any> LiveData<ViewState<X>>.getViewState(
    response: Response<Y>,
    transform: suspend (Y) -> X,
): ViewState<X> {
    val remainingMs = (value as? ViewState.Loading)?.remainingMs ?: 0L
    delay(remainingMs)

    return when (response) {
        is Response.Failure -> getErrorState(response)
        is Response.Success -> try {
            ViewState.Data(transform(response.data))
        } catch (ex: Exception) {
            ViewState.Error(UnexpectedError(underlying = ex), data = value?.data)
        }
    }
}

@MainThread
fun <T : Any, F : Any> LiveData<ViewState<T>>.getErrorState(
    response: Response.Failure<F>,
): ViewState.Error<T> = ViewState.Error(
    error = response.data,
    data = value?.data,
)

fun <X : Any, Y : Any> LiveData<ViewState<X>>.mapState(
    transform: (X) -> Y,
): LiveData<ViewState<Y>> = map { it.mapState(transform) }

suspend fun <T : Any> MutableLiveData<ViewState<T>>.getResponse(
    response: Response<T>,
) = getResponse(
    response = response,
    transform = { it },
)

suspend fun <X : Any, Y : Any> MutableLiveData<ViewState<X>>.getResponse(
    response: Response<Y>,
    transform: suspend (Y) -> X,
) {
    value = getViewState(response, transform)
}

fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    onUpdate: (T) -> Unit,
) {
    observe(owner) { event ->
        event?.getContentIfNotHandled()?.let(onUpdate)
    }
}

@MainThread
inline fun <T : Any> LiveData<T>.observeOnce(
    owner: LifecycleOwner,
    crossinline onChangedOnce: (T?) -> Unit,
) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            onChangedOnce(value)
            removeObserver(this)
        }
    })
}
