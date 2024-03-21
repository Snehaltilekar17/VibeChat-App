plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.vibechat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vibechat"
        minSdk = 28
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }


}

dependencies {


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation ("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.hbb20:ccp:2.5.0")

    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-auth")
    implementation("androidx.activity:activity:1.8.2")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
     implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.google.firebase:firebase-messaging:23.4.1")
    implementation ("com.github.bumptech.glide:glide:4.15.1") // Replace 4.12.0 with the latest version
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2") // Replace 4.12.0 with the latest version
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
   // implementation ("com.google.firebase:firebase-admin:8.0.0")

    //implementation ("com.google.firebase:firebase-database:20.0.1")








}



