package kz.zeme.converter.core.currency.data

class CurrencyApiMapper {

    fun map(from: List<CurrencyApi>): Map<String, String> = buildMap {
        from.forEach { currencyApi ->
            val code = currencyApi
                .takeIf { it.type == CurrencyApi.CurrencyTypeApi.Currency }
                ?.isoCode
                ?.takeIf { it.isNotBlank() }
                ?: return@forEach

            put(code, currencyApi.name.orEmpty())
        }
    }
}
