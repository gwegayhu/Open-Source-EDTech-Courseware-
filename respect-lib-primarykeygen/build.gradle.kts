import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.atomicfu)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.date.time)

            //Needs to be added explicitly to avoid crash on Android
            // See https://github.com/Kotlin/kotlinx-atomicfu/issues/145
            implementation(libs.atomicfu)
        }

        androidMain.dependencies {

        }

        jvmMain.dependencies {

        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
        }

    }
}

android {
    namespace = "world.respect.lib.primarykeygen"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
