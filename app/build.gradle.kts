
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.jhomilmotors.jhomilmotorsfff"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jhomilmotors.jhomilmotorsfff"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.navigation.runtime.ktx)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Convertidor JSON a Kotlin
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Coroutines para llamadas asíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // ViewModel con Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    // OkHttp para interceptores (agregar token automáticamente)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation("com.airbnb.android:lottie-compose:6.4.0")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    implementation(libs.androidx.compose.ui.ui)
    kapt("com.google.dagger:hilt-android-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //para las imagenes:
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Credential Manager (La forma moderna de Google Sign-In)
    implementation("androidx.credentials:credentials:1.2.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Google Maps
    implementation("com.google.maps.android:maps-compose:4.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //UI Testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}