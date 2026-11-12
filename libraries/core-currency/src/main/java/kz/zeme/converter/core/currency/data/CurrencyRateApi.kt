package kz.zeme.converter.core.currency.data

import com.google.gson.annotations.SerializedName

class CurrencyRateApi {

    @SerializedName("base")
    var base: String? = null

    @SerializedName("quote")
    var quote: String? = null

    @SerializedName("rate")
    var rate: Double? = null
}
