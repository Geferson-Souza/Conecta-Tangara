plugins {
    alias(libs.plugins.android.application) // Certifique-se que libs.plugins.android.application está definido no seu version catalog (libs.versions.toml)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.conectatangara" // <--- CORRIGIDO
    compileSdk = 34 // <--- AJUSTADO para uma versão estável comum

    defaultConfig {
        applicationId = "com.conectatangara" // <--- CORRIGIDO
        minSdk = 28
        targetSdk = 34 // <--- AJUSTADO para uma versão estável comum
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
    // Se você estiver usando Kotlin, adicione o bloco kotlinOptions se não estiver presente:
    // kotlinOptions {
    //     jvmTarget = "11" // Ou a versão correspondente à sua sourceCompatibility
    // }
}

// Bloco dependencies corrigido (sem aninhamento)
dependencies {
    // Firebase BoM (Bill of Materials) - Certifique-se de usar a versão mais recente estável
    implementation(platform("com.google.firebase:firebase-bom:33.0.0")) // <--- VERSÃO AJUSTADA (exemplo)

    // Firebase Authentication (usando a forma do Version Catalog, se definida)
    implementation(libs.firebase.auth) // Removida a declaração direta duplicada

    // Outras dependências do Firebase (sem especificar versão, pois o BoM cuida disso)
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")

    // Firebase Google Login
    implementation("com.google.android.gms:play-services-auth:21.3.0") // Verifique a versão mais recente compatível

    // Outras libs (usando Version Catalog)
    implementation(libs.appcompat)
    implementation(libs.material)
    // implementation("androidx.core:core-ktx:1.13.1") // Exemplo de como seria sem o catalog, adicione se necessário
    // implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Exemplo, adicione se necessário


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
