plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.starwarsjunior"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.axians.starshutai"
        minSdk = 24
        targetSdk = 33
        versionCode = 5
        versionName = "0.1"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
/*    viewBinding {
        enable = true
    }*/
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")


    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.jakewharton.threetenabp:threetenabp:1.4.6")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.squareup.okhttp3:okhttp:4.9.1")

    // FastAdapter
    val latestFastAdapterRelease = "5.7.0"
    implementation("com.mikepenz:fastadapter:${latestFastAdapterRelease}")
    implementation("com.mikepenz:fastadapter-extensions-expandable:${latestFastAdapterRelease}")
    implementation("com.mikepenz:fastadapter-extensions-binding:${latestFastAdapterRelease}") // diff util helpers
    implementation("com.mikepenz:fastadapter-extensions-diff:${latestFastAdapterRelease}") // diff util helpers
    implementation("com.mikepenz:fastadapter-extensions-drag:${latestFastAdapterRelease}") // drag support
    implementation("com.mikepenz:fastadapter-extensions-paged:${latestFastAdapterRelease}") // paging support
    implementation("com.mikepenz:fastadapter-extensions-scroll:${latestFastAdapterRelease}") // scroll helpers
    implementation("com.mikepenz:fastadapter-extensions-swipe:${latestFastAdapterRelease}") // swipe support
    implementation("com.mikepenz:fastadapter-extensions-ui:${latestFastAdapterRelease}") // pre-defined ui components
    implementation("com.mikepenz:fastadapter-extensions-utils:${latestFastAdapterRelease}") // needs the `expandable`, `drag` and `scroll` extension.

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("androidx.databinding:databinding-common:8.1.4")

    kapt("com.android.databinding:compiler:3.1.4")
}