package kz.zeme.converter.core.observable

import kotlin.math.max
import kz.zeme.converter.core.error.BaseError

sealed class ViewState<T : Any>(
    open val data: T? = null
) {
    companion object {
        const val LOADER_DEFAULT_DURATION_MS = 500L

        fun Long.getRemainingMs(
            durationMs: Long,
        ): Long {
            val startTimeMs = this
            val elapsedTimeMs = System.currentTimeMillis() - startTimeMs

            return max(durationMs - elapsedTimeMs, 0)
        }
    }

    sealed class Loading<T : Any>(
        override val data: T? = null,
        open val durationMs: Long,
    ) : ViewState<T>(data) {

        private val startTimeMs = System.currentTimeMillis()
        val remainingMs get() = startTimeMs.getRemainingMs(durationMs)

        data class Skeleton<T : Any>(
            override val data: T? = null,
            override val durationMs: Long = LOADER_DEFAULT_DURATION_MS,
        ) : Loading<T>(data, durationMs)

        data class ProgressBar<T : Any>(
            override val data: T? = null,
            override val durationMs: Long = LOADER_DEFAULT_DURATION_MS,
        ) : Loading<T>(data, durationMs)
    }

    data class Data<T : Any>(
        override val data: T
    ) : ViewState<T>(data)

    data class Error<T : Any>(
        val error: BaseError,
        override val data: T? = null
    ) : ViewState<T>(data)
}
