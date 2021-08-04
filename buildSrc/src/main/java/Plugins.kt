object Plugins {
    const val androidApp = "com.android.application"
    const val kotlinAndroid = "android"
    const val kotlinAndroidExt = "android.extensions"

    const val androidBuildToolGradle = "com.android.tools.build:gradle:4.2.1"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    const val navigationSafeArgsGradle =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val navigationSafeArgs = "androidx.navigation.safeargs"

    const val hiltGradle = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val hiltAndroid = "dagger.hilt.android.plugin"
}