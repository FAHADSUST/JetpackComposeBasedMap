plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.android) // Add Hilt plugin
    alias(libs.plugins.google.devtools.ksp) // Add KSP plugin
    alias(libs.plugins.compose.compiler)
    kotlin("kapt") // Apply kapt plugin
}

android {
    namespace = "com.moqochallenge.poi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moqochallenge.poi"
        minSdk = 30
        targetSdk = 35
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
        viewBinding = true
    }
    // Add kapt and ksp options
    kapt {
        correctErrorTypes = true
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Jetpack Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.tooling.preview)

    // ViewModel & Lifecycle
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.ktx)

    // Hilt for Dependency Injection
    implementation(libs.dagger.hilt.android)
    androidTestImplementation(libs.androidx.core.testing)
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit for Network Calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp (for logging API calls)
    implementation(libs.logging.interceptor)

    // Google Maps Compose
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    // Room DB (if needed)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.accompanist.swiperefresh)

    // Testing
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.androidx.test.runner)
    //androidTestImplementation(libs.mockito.android)

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.50")

    testImplementation("org.mockito:mockito-android:4.0.0")

    // Mockito-Kotlin (for better Kotlin support)
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

    // Mockito-Inline (for final methods)
    testImplementation("org.mockito:mockito-inline:5.0.0")

    // JUnit for testing
    testImplementation("junit:junit:4.13.2")

    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.0")

    // AndroidX Core Testing (for ViewModel & LiveData)
    testImplementation("androidx.arch.core:core-testing:2.1.0")

}