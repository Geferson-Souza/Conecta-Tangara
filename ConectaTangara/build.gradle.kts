// Adicione este bloco no topo do seu build.gradle.kts (Projeto)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Adicione o classpath para o secrets plugin
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        // Mantenha outros classpaths que você possa ter aqui
    }
}

// Seu bloco plugins existente (sem o secrets-gradle-plugin)
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}