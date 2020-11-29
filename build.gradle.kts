// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
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