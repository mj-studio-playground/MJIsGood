plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

fun com.android.build.gradle.internal.dsl.DefaultConfig.setDefault() {
    applicationId = Constants.packageName
    minSdkVersion(Constants.minSdk)
    targetSdkVersion(Constants.compileSdk)
    versionCode = Constants.versionCode
    versionName = Constants.versionName

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

android {
    compileSdkVersion(Constants.compileSdk)

    defaultConfig {
        setDefault()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildTypes.forEach { _ ->
        //Add BuildConfig constants
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = Constants.javaVersionStr
    }

    compileOptions {
        sourceCompatibility = Constants.javaVersion
        targetCompatibility = Constants.javaVersion
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    externalNativeBuild {
        cmake {
            path("src/main/jni/CMakeLists.txt")
        }
    }

    lintOptions {
        lintConfig = file("${rootDir}/lint.xml")
        xmlReport = false
        htmlReport = true
        isIgnoreWarnings = false
        isAbortOnError = true
        isWarningsAsErrors = true
    }


    // AGP 4.1 issue !!! JUnit version 3.8 or later expected:
    // https://issuetracker.google.com/issues/170328018
    //    useLibrary("android.test.runner")
    //    useLibrary("android.test.base")
    //    useLibrary("android.test.mock")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    implementation("androidx.core:core-ktx:${Versions.androidxCore}")
    implementation("androidx.appcompat:appcompat:${Versions.androidxAppCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.activity:activity-ktx:${Versions.androidxActivity}")
    implementation("androidx.fragment:fragment-ktx:${Versions.androidxFragment}")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}")

    //Biometric
    implementation("androidx.biometric:biometric:1.1.0-beta01")
    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-alpha02")
    //Security
//    implementation("androidx.security:security-identity-credential:1.0.0-rc03")
    implementation("androidx.security:security-crypto:1.1.0-alpha02")

    //Glide
    implementation("com.github.bumptech.glide:glide:${Versions.glide}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glide}")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidxLifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.androidxLifecycle}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidxLifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.androidxLifecycle}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Versions.androidxLifecycleJava8}")
    testImplementation("androidx.arch.core:core-testing:${Versions.androidxArchCoreTesting}")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.androidxNavigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.androidxNavigation}")

    //Dagger, Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hiltAndroid}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hiltAndroid}")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt}")
    kapt("androidx.hilt:hilt-compiler:${Versions.hilt}")
    kapt("com.squareup:javapoet:${Versions.javapoet}")

    //LoremIpsum
    implementation("com.thedeanda:lorem:2.1")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.5")
}

dependencies {
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.2")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-inline:3.3.3")
    testImplementation("org.robolectric:robolectric:4.3.1")

    // Assertions
    testImplementation("androidx.test.ext:truth:1.3.0")
    testImplementation("com.google.truth:truth:1.0")

    testImplementation("com.google.dagger:hilt-android-testing:2.28-alpha")
    kaptTest("com.google.dagger:hilt-android-compiler:2.28-alpha")

    androidTestImplementation("androidx.test.ext:truth:1.3.0")
    androidTestImplementation("com.google.truth:truth:1.0")

//    androidTestImplementation("androidx.work:work-testing:2.3.4")
    debugImplementation("androidx.fragment:fragment-testing:1.2.5")

    // Core library
    androidTestImplementation("androidx.test:core-ktx:1.3.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.2")

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")

    // Espresso dependencies
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    implementation("androidx.test.espresso:espresso-idling-resource:3.3.0")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.28-alpha")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.28-alpha")
}

tasks.register("lintAppModule") {
    dependsOn(":app:lint")

    doLast {
        println("Lint check success ✅")
    }
}
