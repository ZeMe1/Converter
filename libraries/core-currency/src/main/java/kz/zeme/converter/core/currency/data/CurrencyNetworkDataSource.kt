package kz.zeme.converter.core.currency.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyNetworkDataSource {

    @GET("currencies")
    suspend fun getCurrencies(): Response<List<CurrencyApi>>

    @GET("rates")
    suspend fun getRates(
        @Query("base") base: String,
    ): Response<List<CurrencyRateApi>>
}
