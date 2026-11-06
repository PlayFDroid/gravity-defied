import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val keystorePropertiesFile = rootProject.file("keys/keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) load(keystorePropertiesFile.inputStream())
}

android {
    namespace = "cl.figonzal.gravitydefied"
    compileSdk = 36

    defaultConfig {
        applicationId = "cl.figonzal.gravitydefied"
        minSdk = 23
        targetSdk = 35
        versionCode = 10
        versionName = "1.0.9"
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        resValues = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            resValue("string", "app_name", "Gravity Defied Classic-debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                signingConfig = signingConfigs.getByName("debug")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "app_name", "Gravity Defied Classic")
        }
    }

    bundle {
        // Keep all density resources in the base install instead of density
        // config splits. Prevents Resources$NotFoundException ("Unable to find
        // resource ID") on devices where the density split is not installed
        // (sideloaded base APK / split delivery failures).
        density {
            enableSplit = false
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.androidx.fragment)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
}
