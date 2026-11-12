package kz.zeme.converter.core.currency.data

import com.google.gson.annotations.SerializedName

class CurrencyApi {

    @SerializedName("iso_code")
    var isoCode: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("type")
    var type: CurrencyTypeApi? = null

    enum class CurrencyTypeApi {
        @SerializedName("currency")
        Currency,
    }
}
