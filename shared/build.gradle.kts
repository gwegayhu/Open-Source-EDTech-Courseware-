import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
        }
    }
}

tasks.register<JavaExec>("runOpdsValidator") {
    group = "application"
    description = "Runs the OPDS validator CLI"
    classpath = kotlin.jvm().compilations["main"].output.allOutputs +
            configurations.getByName("jvmRuntimeClasspath")
    mainClass.set("world.respect.OpdsCliApp")

    // CLI arguments pass करना - इसे आप command line से override कर सकते हैं
    // E.g., ./gradlew :shared:runOpdsValidator --args="--catalog /path/to/file.json"
    args = listOf("--help")
}

android {
    namespace = "world.respect.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
