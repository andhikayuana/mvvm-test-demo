package id.yuana.demo.currencyconversion.exception

data class FailedResponseException(
    val success: Boolean,
    val error: Error
) : Exception(error.info)

data class Error(
    val code: Int,
    val type: String,
    val info: String
)