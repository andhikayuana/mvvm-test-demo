package id.yuana.demo.currencyconversion.data.model

data class ExchangeRate(
    val code: String,
    val rate: Float
) {

    fun getFromCode(): String = code.slice(IntRange(0, 2))

    fun getToCode(): String = code.slice(IntRange(3, 5))
}
