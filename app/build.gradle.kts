plugins {
    id(Plugins.androidApp)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinAndroidExt)
    id(Plugins.navigationSafeArgs)
    kotlin("kapt")
    id(Plugins.hiltAndroid)
    id("kotlin-android")
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion = Versions.buildTool

    defaultConfig {
        applicationId = AppVersion.applicationId
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = AppVersion.versionCode()
        versionName = AppVersion.versionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        val baseUrl: String by project
        val apiKey: String by project

        buildTypes.onEach {
            it.buildConfigField("String", "CURRENCY_LAYER_BASE_URL", baseUrl)
            it.buildConfigField("String", "CURRENCY_LAYER_API_KEY", apiKey)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(Deps.kotlinStd)
    implementation(Deps.coreKtx)
    implementation(Deps.appCompat)
    implementation(Deps.material)
    implementation(Deps.constraintLayout)
    implementation(Deps.navigationFragment)
    implementation(Deps.navigationUI)
    implementation(Deps.viewmodel)
    implementation(Deps.livedata)
    implementation(Deps.workmanager)
    implementation(Deps.recyclerView)
    implementation(Deps.hiltAndroid)
    kapt(Deps.hiltCompiler)
    implementation(Deps.hiltWork)
    kapt(Deps.hiltWorkCompiler)


    implementation(Deps.retrofit)
    implementation(Deps.retrofitGsonConverter)
    implementation(Deps.okhttp)
    implementation(Deps.okhttpLogging)


    testImplementation(Deps.junit)
    testImplementation(Deps.mockWebserer)
    testImplementation(Deps.googleTruth)
    testImplementation(Deps.mockito)
    testImplementation(Deps.mockitoInline)
    testImplementation(Deps.androidCoreTest)
    testImplementation(Deps.coroutinesTest)
    testImplementation(Deps.mockk)

    androidTestImplementation(Deps.junitExt)
    androidTestImplementation(Deps.espresso)
}