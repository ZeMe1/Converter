package kz.zeme.converter.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RequestCode : Parcelable {
    RC_PICK_FROM_CURRENCY,
    RC_PICK_TO_CURRENCY,
}
