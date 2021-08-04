package id.yuana.demo.currencyconversion.data

import id.yuana.demo.currencyconversion.data.model.ConvertedRate
import id.yuana.demo.currencyconversion.data.model.Currency
import id.yuana.demo.currencyconversion.data.model.ExchangeRate

class ExchangeRateConverter {

    fun convertToUSD(
        amount: Float,
        currency: Currency,
        exchangeRates: List<ExchangeRate>
    ): Float = amount / exchangeRates.first { it.getToCode() == currency.code }.rate


    fun convertExchangeRates(
        exchangeRates: List<ExchangeRate>,
        supportedCurrencies: List<Currency>,
        amountUSD: Float
    ): List<ConvertedRate> = exchangeRates.map { xRate ->
        ConvertedRate(
            currency = supportedCurrencies.first { it.code == xRate.getToCode() },
            rate = xRate.rate,
            value = amountUSD * xRate.rate
        )
    }.toList()
}