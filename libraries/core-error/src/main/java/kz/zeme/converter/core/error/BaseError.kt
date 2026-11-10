package kz.zeme.converter.core.error

import android.os.Parcelable

abstract class BaseError(
    open val code: ErrorCode,
    open val underlying: Throwable? = null,
    override val message: String? = null,
) : Exception(message, underlying), Parcelable {

    interface ErrorCode : Parcelable

    val name: String
        get() = javaClass.simpleName

    inline fun <reified T : BaseError> getUnderlyingErrorOrNull(): T? {
        var child = underlying as? BaseError
        while (child != null) {
            if (child is T) return child
            child = child.underlying as? BaseError
        }
        return null
    }
}
