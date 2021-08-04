package id.yuana.demo.currencyconversion.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import id.yuana.demo.currencyconversion.data.local.Cache
import id.yuana.demo.currencyconversion.data.remote.CurrencyLayerApi
import id.yuana.demo.currencyconversion.util.MockResponseFileReader
import id.yuana.demo.currencyconversion.util.Resource
import id.yuana.demo.currencyconversion.util.ResponseFile
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class CurrencyLayerRepositoryTest : TestCase() {

    private lateinit var repository: CurrencyLayerRepository
    private lateinit var cache: Cache
    private lateinit var api: CurrencyLayerApi

    @Before
    public override fun setUp() {
        cache = mock(Cache::class.java)
        api = mock(CurrencyLayerApi::class.java)

        repository = CurrencyLayerRepository(api, cache)

    }

    @After
    public override fun tearDown() {
    }

    @Test
    fun `when call getSupportedCurrencies then return success`() {
        runBlocking {

            val rawResponseBody =
                MockResponseFileReader(ResponseFile.RESPONSE_LIST_SUPPORTED_CURRENCIES).content
            val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
            val response = Response.success(responseBody)

            given(api.getSupportedCurrencies()).willReturn(response)

            repository.getSupportedCurrencies()

            verify(api).getSupportedCurrencies()
            verify(cache).saveSupportedCurrencies(responseBody["currencies"].asJsonObject)
            verify(cache).getSupportedCurrencies()
        }
    }


    @Test
    fun `when call getSupportedCurrencies then return error`() {
        runBlocking {
            //given
            val rawResponseBody =
                MockResponseFileReader(ResponseFile.RESPONSE_INVALID_ACCESS_KEY).content
            val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
            val response = Response.success(responseBody)

            val mockApi = mockk<CurrencyLayerApi>()
            val mockCache = mockk<Cache>()
            val repo = CurrencyLayerRepository(mockApi, mockCache)
            coEvery { mockApi.getSupportedCurrencies(any()) } returns response
            every { mockCache.hasSupportedCurrencies() } returns false

            //when
            val result = repo.getSupportedCurrencies()

            //then
            assertEquals(Resource.error(msg = "Oops, something went wrong", data = null), result)
            io.mockk.coVerify {
                mockApi.getSupportedCurrencies(any())
            }
        }
    }

    @Test
    fun `when call getExchangeRates then return success`() {
        runBlocking {

            val rawResponseBody =
                MockResponseFileReader(ResponseFile.RESPONSE_LIVE_EXCHANGE_RATES).content
            val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
            val response = Response.success(responseBody)

            given(api.getExchangeRates()).willReturn(response)

            repository.getExchangeRates()

            verify(api).getExchangeRates()
            verify(cache).saveExchangeRates(responseBody["quotes"].asJsonObject)

        }
    }

    @Test
    fun `when call getExchangeRates then return error`() {
        runBlocking {
            //given
            val rawResponseBody =
                MockResponseFileReader(ResponseFile.RESPONSE_INVALID_ACCESS_KEY).content
            val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
            val response = Response.success(responseBody)

            val mockApi = mockk<CurrencyLayerApi>()
            val mockCache = mockk<Cache>()
            val repo = CurrencyLayerRepository(mockApi, mockCache)
            coEvery { mockApi.getExchangeRates(any()) } returns response

            //when
            val result = repo.getExchangeRates()

            //then
            assertEquals(Resource.error(msg = "Oops, something went wrong", data = null), result)
            io.mockk.coVerify {
                mockApi.getExchangeRates(any())
            }

        }
    }


    @Test
    fun `when call getSupportedCurrenciesCached then return list of currency`() {
        runBlocking {
            val rawResponseBody =
                MockResponseFileReader(ResponseFile.RESPONSE_LIST_SUPPORTED_CURRENCIES).content
            val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
            val supportedCurrencies = Gson().fromJson<Map<String, String>>(
                responseBody["currencies"].asJsonObject,
                object : TypeToken<Map<String, String>>() {}.type
            )

            given(cache.getSupportedCurrencies()).willReturn(supportedCurrencies)

            val result = repository.getSupportedCurrenciesCached()

            verify(cache).getSupportedCurrencies()
            assertEquals(168, result.size)
        }
    }

    @Test
    fun `when call getExchangeRatesCached then return list of exchange rate`() {
        runBlocking {
            val rawResponseBody =
                MockResponseFileReader(ResponseFile.RESPONSE_LIVE_EXCHANGE_RATES).content
            val responseBody = Gson().fromJson(rawResponseBody, JsonObject::class.java)
            val exchangeRates = Gson().fromJson<Map<String, Float>>(
                responseBody["quotes"].asJsonObject,
                object : TypeToken<Map<String, Float>>() {}.type
            )

            given(cache.getExchangeRates()).willReturn(exchangeRates)

            val result = repository.getExchangeRatesCached()

            verify(cache).getExchangeRates()
            assertEquals(168, result.size)
        }
    }
}