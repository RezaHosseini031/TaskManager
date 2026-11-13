
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.legacy.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.rezahosseini.taskmanager"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.rezahosseini.taskmanager"

        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.1"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {

    // --------------------------------------------------
    // Compose BOM
    // --------------------------------------------------

    implementation(platform(libs.androidx.compose.bom))
    testImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))


    // --------------------------------------------------
    // Android Core
    // --------------------------------------------------

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)


    // --------------------------------------------------
    // Jetpack Compose
    // --------------------------------------------------

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // --------------------------------------------------
    // Lifecycle
    // --------------------------------------------------

    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)


    // --------------------------------------------------
    // Navigation
    // --------------------------------------------------

    // Keep old navigation during XML migration
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // New Compose navigation
    implementation(libs.navigation.compose)

    // Hilt Navigation Compose
    implementation(libs.hilt.navigation.compose)


    // --------------------------------------------------
    // Coroutines / Flow
    // --------------------------------------------------

    implementation(libs.coroutines.android)


    // --------------------------------------------------
    // Room Database
    // --------------------------------------------------

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    // Room compiler through KSP
    ksp(libs.room.compiler)


    // --------------------------------------------------
    // Retrofit / Network
    // --------------------------------------------------

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.okhttp.logging)


    // --------------------------------------------------
    // Hilt Dependency Injection
    // --------------------------------------------------

    implementation(libs.hilt.android)

    // Hilt compiler through KSP
    ksp(libs.hilt.compiler)


    // --------------------------------------------------
    // DataStore
    // --------------------------------------------------

    implementation(libs.datastore.preferences)


    // --------------------------------------------------
    // Logger
    // --------------------------------------------------

    implementation(libs.timber)


    // --------------------------------------------------
    // Glide
    // --------------------------------------------------

    implementation(libs.glide)

    // Keep legacy Kapt only for Glide annotation processor
    kapt(libs.glide.compiler)


    // --------------------------------------------------
    // Unit Testing
    // --------------------------------------------------

    testImplementation(libs.junit)
    testImplementation(libs.mockk)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)


    // --------------------------------------------------
    // Hilt Testing
    // --------------------------------------------------

    testImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android.testing)

    kspTest(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.compiler)


    // --------------------------------------------------
    // Room Testing
    // --------------------------------------------------

    androidTestImplementation(libs.room.testing)


    // --------------------------------------------------
    // Android Instrumentation Testing
    // --------------------------------------------------

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)


    // --------------------------------------------------
    // Compose UI Testing
    // --------------------------------------------------

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )
}
