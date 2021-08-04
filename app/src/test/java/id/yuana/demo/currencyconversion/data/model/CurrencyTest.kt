package id.yuana.demo.currencyconversion.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyTest {

    companion object {
        const val DUMMY_CODE = "IDR"
        const val DUMMY_LABEL = "Indonesian Rupiah"
    }


    @Test
    fun `test currency object return custom return toString()`() {
        val currency = Currency(code = DUMMY_CODE, label = DUMMY_LABEL)

        assertEquals(DUMMY_CODE, currency.code)
        assertEquals(DUMMY_LABEL, currency.label)
        assertEquals("$DUMMY_CODE : $DUMMY_LABEL", currency.toString())
    }
}