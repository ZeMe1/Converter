package kz.zeme.converter.core.observable

import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class EventFlow<T : Any> {
    protected val channel = Channel<T>(Channel.BUFFERED)

    @MainThread
    fun observeEvent(
        owner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
        onUpdate: (T) -> Unit
    ) {
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(minActiveState) {
                for (event in channel) {
                    onUpdate(event)
                }
            }
        }
    }
}

class MutableEventFlow<T : Any>(
    private val scope: CoroutineScope
) : EventFlow<T>() {

    fun asEventFlow(): EventFlow<T> = this

    var value: T?
        get() = null
        set(event) {
            event?.let { channel.trySend(it) }
        }

    fun postValue(event: T) {
        channel.trySend(event)
    }

    fun <S> addSource(
        source: LiveData<S>,
        observer: (S) -> Unit
    ) {
        source.asFlow()
            .onEach { observer(it) }
            .launchIn(scope)
    }
}
