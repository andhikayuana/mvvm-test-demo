package id.yuana.demo.currencyconversion.data

import id.yuana.demo.currencyconversion.data.model.ConvertedRate
import id.yuana.demo.currencyconversion.data.model.Currency
import id.yuana.demo.currencyconversion.data.model.ExchangeRate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExchangeRateConverterTest {

    private lateinit var exchangeRateConverter: ExchangeRateConverter

    @Before
    fun setUp() {
        exchangeRateConverter = ExchangeRateConverter()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `when call convertToUSD() params are filled correctly, then should success`() {
        //given
        val amount: Float = 1000000F
        val currency: Currency = Currency(code = "IDR", label = "Indonesian Rupiah")

        val exchangeRates: List<ExchangeRate> = listOf(
            ExchangeRate(
                code = "USDIDR",
                rate = 14487.25F
            ),
            ExchangeRate(
                code = "USDSGD",
                rate = 1.35105F
            ),
            ExchangeRate(
                code = "USDJPY",
                rate = 110.12404F
            )
        )

        //when
        val result = exchangeRateConverter.convertToUSD(
            amount = amount,
            currency = currency,
            exchangeRates = exchangeRates
        )

        //then
        assertEquals(69.026215F, result)
    }

    @Test
    fun `when call convertExchangeRates() paramse are filled correctly, then should success`() {
        //given
        val exchangeRates: List<ExchangeRate> = listOf(
            ExchangeRate(
                code = "USDIDR",
                rate = 14487.25F
            ),
            ExchangeRate(
                code = "USDSGD",
                rate = 1.35105F
            ),
            ExchangeRate(
                code = "USDJPY",
                rate = 110.12404F
            )
        )
        val supportedCurrencies: List<Currency> = listOf(
            Currency(code = "IDR", label = "Indonesian Rupiah"),
            Currency(code = "SGD", label = "Singapore Dollar"),
            Currency(code = "JPY", label = "Japanese Yen")
        )
        val amountUSD = 6F

        //when
        val actualResult = exchangeRateConverter.convertExchangeRates(
            exchangeRates = exchangeRates,
            supportedCurrencies = supportedCurrencies,
            amountUSD = amountUSD
        )

        //then
        val expectedResult = listOf(
            ConvertedRate(
                currency = supportedCurrencies[0],
                rate = exchangeRates[0].rate,
                value = amountUSD * exchangeRates[0].rate
            ),
            ConvertedRate(
                currency = supportedCurrencies[1],
                rate = exchangeRates[1].rate,
                value = amountUSD * exchangeRates[1].rate
            ),
            ConvertedRate(
                currency = supportedCurrencies[2],
                rate = exchangeRates[2].rate,
                value = amountUSD * exchangeRates[2].rate
            ),
        )

        assertEquals(expectedResult, actualResult)
    }
}