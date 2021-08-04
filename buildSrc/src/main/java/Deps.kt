object Deps {

    const val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"

    //android
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val workmanager = "androidx.work:work-runtime-ktx:${Versions.workmanager}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltWork = "androidx.hilt:hilt-work:${Versions.hiltWork}"
    const val hiltWorkCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltWork}"


    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"


    //test
    const val junit = "junit:junit:4.+"
    const val junitExt = "androidx.test.ext:junit:1.1.2"
    const val espresso = "androidx.test.espresso:espresso-core:3.3.0"
    const val mockWebserer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
    const val googleTruth = "com.google.truth:truth:${Versions.googleTruth}"
    const val mockito = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockito}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val androidCoreTest = "androidx.arch.core:core-testing:${Versions.androidCoreTest}"
    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
}

object Versions {

    const val kotlin = "1.5.10"
    const val compileSdk = 30
    const val buildTool = "30.0.3"

    const val minSdk = 21
    const val targetSdk = 30


    //android
    const val coreKtx = "1.5.0"
    const val appCompat = "1.3.0"
    const val material = "1.3.0"
    const val constraintLayout = "2.0.4"
    const val lifecycle = "2.3.1"
    const val workmanager = "2.5.0"
    const val navigation = "2.3.5"
    const val recyclerView = "1.2.1"
    const val hilt = "2.35"
    const val hiltWork = "1.0.0"

    const val retrofit = "2.9.0"
    const val okhttp = "4.9.0"

    const val googleTruth = "1.1.2"
    const val mockito = "3.8.0"
    const val androidCoreTest = "2.1.0"
    const val coroutinesTest = "1.3.4"
    const val mockk = "1.12.0"

}