package id.yuana.demo.currencyconversion.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import id.yuana.demo.currencyconversion.data.local.Cache
import id.yuana.demo.currencyconversion.data.model.Currency
import id.yuana.demo.currencyconversion.data.model.ExchangeRate
import id.yuana.demo.currencyconversion.data.remote.CurrencyLayerApi
import id.yuana.demo.currencyconversion.exception.FailedResponseException
import id.yuana.demo.currencyconversion.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyLayerRepository @Inject constructor(
    private val api: CurrencyLayerApi,
    private val cache: Cache
) {

    suspend fun getSupportedCurrencies(): Resource<Map<String, String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getSupportedCurrencies()

                response.body()?.let {
                    val success = (it["success"] as JsonPrimitive).asBoolean
                    if (success) {
                        cache.saveSupportedCurrencies(it["currencies"] as JsonObject)
                    } else {
                        val error = Gson().fromJson(it, FailedResponseException::class.java)
                        throw error
                    }
                }

                Resource.success(cache.getSupportedCurrencies())
            } catch (e: Exception) {
                if (cache.hasSupportedCurrencies()) {
                    Resource.success(cache.getSupportedCurrencies())
                } else {
                    Resource.error(data = null, msg = e.message ?: "Oops, something went wrong")
                }
            }
        }
    }

    suspend fun getExchangeRates(): Resource<Map<String, Float>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getExchangeRates()
                response.body()?.let {
                    val success = (it["success"] as JsonPrimitive).asBoolean
                    if (success) {
                        cache.saveExchangeRates(it["quotes"] as JsonObject)
                    } else {
                        val error = Gson().fromJson(it, FailedResponseException::class.java)
                        throw error
                    }
                }

                Resource.success(cache.getExchangeRates())

            } catch (e: Exception) {
                Resource.error(data = null, msg = e.message ?: "Oops, something went wrong")
            }
        }
    }

    suspend fun getSupportedCurrenciesCached(): List<Currency> {
        return withContext(Dispatchers.IO) {
            cache.getSupportedCurrencies().map {
                Currency(code = it.key, label = it.value)
            }.toList()
        }
    }

    suspend fun getExchangeRatesCached(): List<ExchangeRate> {
        return withContext(Dispatchers.IO) {
            cache.getExchangeRates().map {
                ExchangeRate(
                    code = it.key,
                    rate = it.value
                )
            }.toList()
        }
    }
}