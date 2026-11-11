package kz.zeme.converter.core.navigation

import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kz.zeme.converter.core.error.BaseError

@Parcelize
sealed class BaseNavigation : Navigator.Navigation {
    data object GoBack : BaseNavigation()
    data object Close : BaseNavigation()

    @Parcelize
    sealed class ShowToast : BaseNavigation() {
        data class Message(val msg: String) : ShowToast()
        data class MessageErr(val error: BaseError) : ShowToast()
        data class MessageRes(@param:StringRes val msgRes: Int) : ShowToast()
    }

    data class ShowMain(
        val restart: Boolean = false,
        val redirect: Navigator.Navigation? = null,
        val withSplash: Boolean = false,
    ) : BaseNavigation()

    data class ShowDeeplink(
        val url: String,
        val extras: String? = null,
    ) : BaseNavigation()
}
