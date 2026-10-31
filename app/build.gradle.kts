
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "au.edu.jcu.assessment.utilityapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "au.edu.jcu.assessment.utilityapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Material icons
    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    // ViewModel and lifecycle-aware state
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2"
    )
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2"
    )
    implementation(
        "androidx.lifecycle:lifecycle-runtime-compose:2.9.2"
    )

    // Retrofit for REST API requests
    implementation(
        "com.squareup.retrofit2:retrofit:2.11.0"
    )

    // Gson JSON converter
    implementation(
        "com.squareup.retrofit2:converter-gson:2.11.0"
    )

    // Coil for Pokemon artwork
    implementation(
        "io.coil-kt:coil-compose:2.7.0"
    )

    // Unit testing
    testImplementation(libs.junit)

    // Android instrumented testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
