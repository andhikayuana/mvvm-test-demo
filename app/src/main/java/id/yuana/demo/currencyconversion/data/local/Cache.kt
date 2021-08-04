package id.yuana.demo.currencyconversion.data.local

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        const val CACHE_NAME = "currency_conversion"
        const val CACHE_SUPPORTED_CURRENCIES = "cache_supported_currencies"
        const val CACHE_EXCHANGE_RATES = "cache_exchange_rates"
    }

    private val pref = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE)

    fun saveSupportedCurrencies(value: JsonObject) {
        pref.edit {
            putString(CACHE_SUPPORTED_CURRENCIES, value.toString())
        }
    }

    fun getSupportedCurrencies(): Map<String, String> {
        val value = pref.getString(CACHE_SUPPORTED_CURRENCIES, "{}")
        return Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)
    }

    fun hasSupportedCurrencies(): Boolean = getSupportedCurrencies().isNotEmpty()

    fun saveExchangeRates(value: JsonObject) {
        pref.edit {
            putString(CACHE_EXCHANGE_RATES, value.toString())
        }
    }

    fun getExchangeRates(): Map<String, Float> {
        val value = pref.getString(CACHE_EXCHANGE_RATES, "{}")
        return Gson().fromJson(value, object : TypeToken<Map<String, Float>>() {}.type)
    }

    fun hasExchangeRates(): Boolean = getExchangeRates().isNotEmpty()
}