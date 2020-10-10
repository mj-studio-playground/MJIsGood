// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidGradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")

        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltAndroid}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidxNavigation}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    val clean by registering(Delete::class){
        delete(buildDir)
    }
}