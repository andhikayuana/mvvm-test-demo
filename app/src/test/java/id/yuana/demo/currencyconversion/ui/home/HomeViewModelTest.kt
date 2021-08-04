package id.yuana.demo.currencyconversion.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import id.yuana.demo.currencyconversion.data.ExchangeRateConverter
import id.yuana.demo.currencyconversion.data.model.ConvertedRate
import id.yuana.demo.currencyconversion.data.model.Currency
import id.yuana.demo.currencyconversion.data.model.ExchangeRate
import id.yuana.demo.currencyconversion.data.repository.CurrencyLayerRepository
import id.yuana.demo.currencyconversion.util.CoroutinesTestRule
import id.yuana.demo.currencyconversion.util.MockResponseFileReader
import id.yuana.demo.currencyconversion.util.Resource
import id.yuana.demo.currencyconversion.util.ResponseFile
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val testCoroutineRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    private lateinit var repository: CurrencyLayerRepository

    @MockK
    private lateinit var exchangeRateConverter: ExchangeRateConverter

    @MockK
    private lateinit var supportedCurrenciesObserver: Observer<List<Currency>>

    @MockK
    private lateinit var convertedRatesObserver: Observer<Resource<List<ConvertedRate>>>

    @MockK
    private lateinit var onInputChangedObserver: Observer<Unit>

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)


        coEvery { repository.getSupportedCurrenciesCached() } returns getSupportedCurrencies()
        coEvery { repository.getExchangeRatesCached() } returns getExchangeRates()
        every { supportedCurrenciesObserver.onChanged(getSupportedCurrencies()) } just Runs
        every { onInputChangedObserver.onChanged(Unit) } just Runs

        viewModel = HomeViewModel(repository, exchangeRateConverter)
        viewModel.onInputChanged.observeForever(onInputChangedObserver)
        viewModel.supportedCurrencies.observeForever(supportedCurrenciesObserver)

    }

    private fun getSupportedCurrencies(): List<Currency> {
        val rawResponseBody =
            MockResponseFileReader(ResponseFile.RESPONSE_LIST_SUPPORTED_CURRENCIES).content
        val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
        val supportedCurrencies = Gson().fromJson<Map<String, String>>(
            responseBody["currencies"].asJsonObject,
            object : TypeToken<Map<String, String>>() {}.type
        )
        return supportedCurrencies.map {
            Currency(code = it.key, label = it.value)
        }.toList()
    }

    private fun getExchangeRates(): List<ExchangeRate> {
        val rawResponseBody =
            MockResponseFileReader(ResponseFile.RESPONSE_LIVE_EXCHANGE_RATES).content
        val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
        val exchangeRates = Gson().fromJson<Map<String, Float>>(
            responseBody["quotes"].asJsonObject,
            object : TypeToken<Map<String, Float>>() {}.type
        )
        return exchangeRates.map {
            ExchangeRate(
                code = it.key,
                rate = it.value
            )
        }.toList()
    }

    @After
    fun tearDown() {
        viewModel.onInputChanged.removeObserver(onInputChangedObserver)
        viewModel.supportedCurrencies.removeObserver(supportedCurrenciesObserver)
    }

    @Test
    fun `test call onConvert then should success`() {
        testCoroutineRule.runBlockingTest {
            //given

            val selectedCurrency = Currency(code = "USD", label = "United States Dollar")
            val amount = 1F

            coEvery { repository.getExchangeRatesCached() } returns getExchangeRates()
            every {
                exchangeRateConverter.convertToUSD(
                    amount,
                    selectedCurrency,
                    getExchangeRates()
                )
            } returns 1F
            every {
                exchangeRateConverter.convertExchangeRates(
                    any(),
                    any(),
                    any()
                )
            } returns listOf()
            every { convertedRatesObserver.onChanged(any()) } just Runs

            //when
            viewModel.convertedRates.observeForever(convertedRatesObserver)
            viewModel.onConvert(selectedCurrency, amount)

            //then
            verifyAll {
                onInputChangedObserver.onChanged(Unit)
                convertedRatesObserver.onChanged(Resource.loading(null))
                convertedRatesObserver.onChanged(Resource.success(emptyList()))
            }
            viewModel.convertedRates.removeObserver(convertedRatesObserver)
        }
    }
}