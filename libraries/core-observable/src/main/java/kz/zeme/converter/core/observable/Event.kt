package kz.zeme.converter.core.observable

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? =
        if (hasBeenHandled) null else content.also { hasBeenHandled = true }

    fun peekContent(): T = content
}
