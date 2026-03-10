plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
}

android {
    namespace = "dev.aleksrychkov.tetromino"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    signingConfigs {
        create("_debug") {
            val keystore =
                project.layout.projectDirectory.file("_debug.jks").asFile
            storeFile = keystore
            storePassword = "_debug"
            keyAlias = "_debug"
            keyPassword = "_debug"
        }
    }

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "dev.aleksrychkov.tetromino"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("_debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.ui)

    debugImplementation(libs.androidx.ui.tooling)
}