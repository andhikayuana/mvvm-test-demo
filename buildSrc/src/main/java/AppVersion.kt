object AppVersion {
    const val applicationId = "id.yuana.demo.currencyconversion"
    const val major = 1
    const val minor = 0
    const val patch = 0
    const val build = 0

    fun versionCode(): Int = major * 10000 + minor * 1000 + patch * 100 + build
    fun versionName(): String = "${major}.${minor}.${patch}"
}