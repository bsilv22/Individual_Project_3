plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.zybooks.individual_project3_game"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zybooks.individual_project3_game"
        minSdk = 31
        targetSdk = 34
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
    implementation ("androidx.compose.material3:material3:1.1.1")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.0")
    implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.runtime:runtime:1.6.1")
    implementation ("androidx.compose.ui:ui:1.6.1")
    implementation ("androidx.activity:activity-compose:1.8.2")
    implementation ("androidx.compose.ui:ui:1.4.0")
    implementation ("androidx.compose.foundation:foundation:1.4.0")
    implementation ("androidx.compose.material3:material3:1.0.0")
    implementation ("com.google.android.material:material:1.5.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}