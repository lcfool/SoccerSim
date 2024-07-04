plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.soccersim.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.android.koin)

    implementation(libs.commons.math3)

    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Mockito for mocking
    testImplementation(libs.mockito.core)
    testImplementation (libs.mockito.inline)

    // Kotlin Coroutines for testing
    testImplementation (libs.kotlinx.coroutines.test)

    // AndroidX Test - Core dependencies
    testImplementation (libs.androidx.core)
    testImplementation (libs.androidx.junit)
    testImplementation (libs.androidx.rules)

    // AndroidX Test - Runner dependencies
    testImplementation (libs.androidx.runner)

    // Mockk for Kotlin
    testImplementation (libs.mockk)
}