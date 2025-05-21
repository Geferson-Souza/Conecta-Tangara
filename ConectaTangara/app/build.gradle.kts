plugins {
    alias(libs.plugins.android.application)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.conectatangara"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.conectatangara"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    dependencies {
        // Firebase BoM
        implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
        // Firebase google login
        implementation("com.google.android.gms:play-services-auth:21.3.0")

        // Firebase libs (sem versão)
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore")

        // Outras libs
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.firebase.auth)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
    }
}