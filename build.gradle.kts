// Top-level build.gradle.kts
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // Android Gradle Plugin
        classpath("com.android.tools.build:gradle:8.6.0")

        // Kotlin Gradle Plugin
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    }
}

// Optional: plugins DSL alternative
plugins {
    // You could also define plugins here if needed
}
