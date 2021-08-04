package id.yuana.demo.currencyconversion.data.model

data class Currency(
    val code: String,
    val label: String
) {

    override fun toString(): String = "$code : $label"

}
