package id.yuana.demo.currencyconversion.data.remote

import com.google.gson.JsonObject
import id.yuana.demo.currencyconversion.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerApi {

    @GET("/list")
    suspend fun getSupportedCurrencies(
        @Query("access_key") accessKey: String = BuildConfig.CURRENCY_LAYER_API_KEY
    ): Response<JsonObject>

    @GET("/live")
    suspend fun getExchangeRates(
        @Query("access_key") accessKey: String = BuildConfig.CURRENCY_LAYER_API_KEY
    ): Response<JsonObject>

}