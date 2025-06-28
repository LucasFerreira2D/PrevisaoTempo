plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "br.com.trabalhofinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.trabalhofinal"
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation (libs.material.v190)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.gson)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    
    implementation (libs.recyclerview)

    implementation (libs.play.services.location)

    implementation (libs.room.runtime)
    annotationProcessor (libs.room.compiler)
}