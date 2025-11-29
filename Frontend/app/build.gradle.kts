plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.bookstore"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.bookstore"
        minSdk = 24
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
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Jetpack Compose BOM (to manage versions consistently)
    implementation(platform("androidx.compose:compose-bom:2025.09.01"))

// Core UI + Material 3
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

// For using Compose in Fragments/Activities
    implementation("androidx.activity:activity-compose")
    implementation("androidx.fragment:fragment-ktx")

// (Optional) Compose Navigation, if later you want pure Compose navigation
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("androidx.compose.material:material-icons-extended")

    // Dùng để cho phép Retrofit sử dụng Gson để chuyển đổi JSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Thư viện Coroutines Test cho môi trường Unit Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("com.google.android.gms:play-services-auth:21.0.0")
}