
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.futsal"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.futsal"
        minSdk = 26
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.androidx.navigation.compose)
    implementation("com.cloudinary:cloudinary-android:2.1.0")
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.30.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Android instrumented testing
    androidTestImplementation ("androidx.test:runner:1.2.0")
    androidTestImplementation ("androidx.test:rules:1.2.0")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")

/// Compose UI testing / debug
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.0")

    // Unit testing
    testImplementation("junit:junit:4.13.2")



}
