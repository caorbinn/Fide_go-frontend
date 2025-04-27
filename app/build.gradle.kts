plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.fide_go"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fide_go"
        minSdk = 24
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
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Jetpack Credential Manager
    implementation("androidx.credentials:credentials:1.2.2")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    // Google ID Helper Library para Sign-in with Google
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    //localdate
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3") // o la última versión disponible


    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    val compose_version = "1.0.2"

    //FIREBASE
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.android.gms:play-services-auth:20.6.0")


    //COIL
    implementation("io.coil-kt:coil:1.4.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))

    //DATABASE
    // Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")


    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    // Kotlin components
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.0")
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //RETROFIT
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //Gson
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //GOOGLE
    // Auth completo (útil para OAuth, Firebase Auth, etc.)
    implementation("com.google.android.gms:play-services-auth:20.8.0")

    // ——> Necesario para One-Tap / Identity API:
    implementation("com.google.android.gms:play-services-auth-api-identity:1.7.0")
    // Google One-Tap / Identity API
    //implementation("com.google.android.gms:play-services-auth-api-identity:1.8.0")




    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material:$compose_version")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}