package id.yuana.demo.currencyconversion.data.model

import org.junit.Assert.*
import org.junit.Test

class ExchangeRateTest {

    companion object {
        const val DUMMY_CODE: String = "USDIDR"
        const val DUMMY_RATE: Float = 14577.9.toFloat()
    }

    @Test
    fun `test exchangeRate object return as expected`() {
        val exchangeRate = ExchangeRate(code = DUMMY_CODE, rate = DUMMY_RATE)

        assertEquals(6, exchangeRate.code.length)
        assertEquals(DUMMY_CODE, exchangeRate.code)
        assertEquals(DUMMY_RATE, exchangeRate.rate)

        assertEquals(3, exchangeRate.getFromCode().length)
        assertEquals("USD", exchangeRate.getFromCode())

        assertEquals(3, exchangeRate.getToCode().length)
        assertEquals("IDR", exchangeRate.getToCode())


    }
}