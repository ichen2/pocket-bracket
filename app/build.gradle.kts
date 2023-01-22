plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.apollographql.apollo3").version("3.7.3")
}
apollo {
    service("service") {
        packageName.set("com.ichen.pocketbracket")
    }
}
android {
    signingConfigs {
        create("release") {
            keyAlias = "pocket-bracket-key"
            keyPassword = "pbandj43"
            storeFile = file("/Users/ichen/AndroidKeys")
            storePassword = "pbandj43"
        }
    }
    compileSdk = 33
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.ichen.pocketbracket"
        minSdk = 21
        targetSdk = 31
        versionCode = 12
        versionName = "1.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = false
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
}

val accompanistVersion = "0.28.0"

dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("com.apollographql.apollo3:apollo-runtime:3.7.3")
    implementation("com.apollographql.apollo3:apollo-api:3.7.3")
    implementation("androidx.compose.material:material-icons-extended:${rootProject.extra["compose_version"]}")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets-ui:$accompanistVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0-alpha03")
    implementation("com.squareup:android-times-square:1.6.5@aar")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
    implementation("io.github.vanpra.compose-material-dialogs:core:0.6.0")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.6.0")
    implementation("io.coil-kt:coil-compose:1.3.2")
    implementation("androidx.navigation:navigation-compose:2.6.0-alpha04")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
}