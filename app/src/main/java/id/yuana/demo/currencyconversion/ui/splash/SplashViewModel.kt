package id.yuana.demo.currencyconversion.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.demo.currencyconversion.data.repository.CurrencyLayerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: CurrencyLayerRepository
) : ViewModel() {

    val loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun fetchSupportredCurrencies(onSuccess: () -> Unit, onError: (message: String?) -> Unit) {
        viewModelScope.launch {
            loading.value = true
            delay(3000)
            if (repository.getSupportedCurrenciesCached().isEmpty()) {
                val response = repository.getSupportedCurrencies()
                if (!response.isSuccess()) {
                    onError(response.message)
                }
            }
            loading.value = false
            onSuccess()
        }
    }

}