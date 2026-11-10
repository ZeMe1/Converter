package kz.zeme.converter.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RequestCode : Parcelable {
    RC_CONFIRM_ACTION,
    RC_ITEM_DETAILS,
}
