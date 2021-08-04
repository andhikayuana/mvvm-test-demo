package id.yuana.demo.currencyconversion.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.demo.currencyconversion.data.ExchangeRateConverter
import id.yuana.demo.currencyconversion.data.model.ConvertedRate
import id.yuana.demo.currencyconversion.data.model.Currency
import id.yuana.demo.currencyconversion.data.repository.CurrencyLayerRepository
import id.yuana.demo.currencyconversion.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CurrencyLayerRepository,
    private val exchangeRateConverter: ExchangeRateConverter
) : ViewModel() {

    val amount: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>(1F)
    }
    val selectedCurrency: MutableLiveData<Currency> by lazy {
        MutableLiveData<Currency>(
            Currency(
                code = "USD",
                label = "United States Dollar"
            )
        )
    }

    val supportedCurrencies: MutableLiveData<List<Currency>> by lazy {
        MutableLiveData<List<Currency>>(listOf())
    }
    val convertedRates: MutableLiveData<Resource<List<ConvertedRate>>> by lazy {
        MutableLiveData<Resource<List<ConvertedRate>>>(Resource.success(listOf()))
    }
    val onInputChanged = Transformations.switchMap(amount) { am ->
        Transformations.map(selectedCurrency) { selectedCurrency ->
            onConvert(selectedCurrency, am)
        }
    }


    init {
        viewModelScope.launch {
            supportedCurrencies.value = repository.getSupportedCurrenciesCached()
        }

    }

    fun onConvert(currency: Currency, amount: Float) {
        viewModelScope.launch {
            convertedRates.value = Resource.loading(null)

            val result = withContext(Dispatchers.IO) {
                val exchangeRates = repository.getExchangeRatesCached()
                val amountUSD = exchangeRateConverter.convertToUSD(
                    amount = amount,
                    currency = currency,
                    exchangeRates = exchangeRates
                )
                val supportedCurrencies = repository.getSupportedCurrenciesCached()

                exchangeRateConverter.convertExchangeRates(
                    exchangeRates = exchangeRates,
                    supportedCurrencies = supportedCurrencies,
                    amountUSD = amountUSD
                )
            }

            convertedRates.value = Resource.success(data = result)
        }

    }


}