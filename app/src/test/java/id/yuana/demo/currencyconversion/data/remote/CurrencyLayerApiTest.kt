package id.yuana.demo.currencyconversion.data.remote

import id.yuana.demo.currencyconversion.di.AppModule
import id.yuana.demo.currencyconversion.util.MockResponseFileReader
import id.yuana.demo.currencyconversion.util.ResponseFile.Companion.RESPONSE_INVALID_ACCESS_KEY
import id.yuana.demo.currencyconversion.util.ResponseFile.Companion.RESPONSE_LIST_SUPPORTED_CURRENCIES
import id.yuana.demo.currencyconversion.util.ResponseFile.Companion.RESPONSE_LIVE_EXCHANGE_RATES
import id.yuana.demo.currencyconversion.util.ResponseFile.Companion.RESPONSE_MISSING_ACCESS_KEY
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class CurrencyLayerApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: CurrencyLayerApi

    @Before
    fun setUp() {

        mockWebServer = MockWebServer()
        mockWebServer.start(9090)

        val okHttpClient = AppModule.provideOkHttpClient()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://localhost:9090")
            .client(okHttpClient)
            .build()
        api = AppModule.provideCurrencyLayerApi(retrofit)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun createMockResponse(
        fileName: String,
        responseCode: Int = HttpURLConnection.HTTP_OK
    ) = MockResponse()
        .setResponseCode(responseCode)
        .setBody(MockResponseFileReader(fileName).content)


    @Test
    fun `when get list of supported currencies then response missing access key`() {
        mockWebServer.enqueue(createMockResponse(RESPONSE_MISSING_ACCESS_KEY))

        val actualResponse = runBlocking {
            api.getSupportedCurrencies("")
        }
        val responseBody = actualResponse.body()

        assertNotNull(actualResponse)
        responseBody?.let { body ->
            assertEquals(false, body["success"].asBoolean)

            val error = body["error"].asJsonObject
            assertEquals(101, error["code"].asInt)
            assertEquals("missing_access_key", error["type"].asString)
        }
    }


    @Test
    fun `when get list supported currencies then response success`() {
        mockWebServer.enqueue(createMockResponse(RESPONSE_LIST_SUPPORTED_CURRENCIES))

        val actualResponse = runBlocking {
            api.getSupportedCurrencies()
        }
        val responseBody = actualResponse.body()

        assertNotNull(actualResponse)
        responseBody?.let { body ->
            assertEquals(true, body["success"].asBoolean)
            assertEquals("https://currencylayer.com/terms", body["terms"].asString)
            assertEquals("https://currencylayer.com/privacy", body["privacy"].asString)

            val currencies = body["currencies"].asJsonObject
            assertTrue(currencies.has("IDR"))
            assertTrue(currencies.has("USD"))
        }
    }

    @Test
    fun `when get live exchange rates then response invalid access key`() {
        mockWebServer.enqueue(createMockResponse(RESPONSE_INVALID_ACCESS_KEY))

        val actualResponse = runBlocking {
            api.getSupportedCurrencies("invalidaccesskeyhere")
        }
        val responseBody = actualResponse.body()

        assertNotNull(actualResponse)
        responseBody?.let { body ->
            assertEquals(false, body["success"].asBoolean)

            val error = body["error"].asJsonObject
            assertEquals(101, error["code"].asInt)
            assertEquals("invalid_access_key", error["type"].asString)
        }
    }

    @Test
    fun `when get live exchange rates then response success`() {
        mockWebServer.enqueue(createMockResponse(RESPONSE_LIVE_EXCHANGE_RATES))

        val actualResponse = runBlocking {
            api.getExchangeRates()
        }
        val responseBody = actualResponse.body()

        assertNotNull(actualResponse)
        responseBody?.let { body ->
            assertEquals(true, body["success"].asBoolean)
            assertEquals("https://currencylayer.com/terms", body["terms"].asString)
            assertEquals("https://currencylayer.com/privacy", body["privacy"].asString)
            assertEquals("USD", body["source"].asString)

            val quotes = body["quotes"].asJsonObject
            assertTrue(quotes.has("USDIDR"))
            assertTrue(quotes.has("USDUSD"))
        }
    }
}