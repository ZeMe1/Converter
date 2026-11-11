package kz.zeme.converter.core.navigation

import androidx.lifecycle.MutableLiveData
import kz.zeme.converter.core.domain.RequestCode
import kz.zeme.converter.core.navigation.NavigationResultLiveData.NavigationResult
import kz.zeme.converter.core.observable.Event

class NavigationResultLiveData : MutableLiveData<Event<NavigationResult>>() {

    sealed class NavigationResult(
        open val requestCode: RequestCode,
        open val data: Any? = null,
    ) {

        data class ActivityResult(
            override val requestCode: RequestCode,
            override val data: Any? = null,
            val requestResultCode: Int,
        ) : NavigationResult(requestCode, data)

        sealed class DialogResult(
            override val requestCode: RequestCode,
            override val data: Any? = null,
        ) : NavigationResult(requestCode, data) {

            data class Canceled(
                override val requestCode: RequestCode,
                override val data: Any? = null,
            ) : DialogResult(requestCode)

            data class Dismissed(
                override val requestCode: RequestCode,
            ) : DialogResult(requestCode)

            data class Ok(
                override val requestCode: RequestCode,
                override val data: Any? = null,
            ) : DialogResult(requestCode, data)
        }
    }

    fun setResult(result: NavigationResult) {
        this.postValue(Event(result))
    }

    fun setResult(requestCode: RequestCode, resultCode: Int, data: Any?) {
        val result = NavigationResult.ActivityResult(
            requestCode = requestCode,
            data = data,
            requestResultCode = resultCode,
        )

        setResult(result)
    }
}
