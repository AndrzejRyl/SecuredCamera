object Versions {
    const val minSdk = 23
    const val compileSdk = 30
    const val targetSdk = 30

    const val gradle = "3.6.1"
    const val googlePlayServices = "4.3.3"
    const val kotlin = "1.3.70"
    const val kotlinCoroutines = "1.3.3"

    const val appCompat = "1.1.0"
    const val coreKtx = "1.1.0"
    const val constraintLayout = "2.0.4"
    const val lifecycle = "2.2.0"
    const val materialComponents = "1.3.0-alpha03"

    const val koin = "2.1.5"
}

@Suppress("unused")
object Libs {
    const val gradle_classpath = "com.android.tools.build:gradle:${Versions.gradle}"
    const val google_services_classpath = "com.google.gms:google-services:${Versions.googlePlayServices}"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlin_classpath = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlin_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    const val androidX_appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val androidX_coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    const val androidX_lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val androidX_lifecycle_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val androidX_lifecycle_vmx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val androidX_lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

    const val androidX_constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    const val material_components = "com.google.android.material:material:${Versions.materialComponents}"

    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koin_view_model = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
}