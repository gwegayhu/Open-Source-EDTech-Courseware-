import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
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
            api(projects.respectDatalayer)
            implementation(libs.kotlinx.serialization.json)
            api(libs.uri.kmp)
            api(libs.kotlinx.date.time)
            api(libs.ktor.client.core)
        }

        jvmMain.dependencies {

        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.respectLibXxhash)
            implementation(projects.respectLibUtil)
            implementation(libs.turbine)
            implementation(projects.respectDatalayerHttp)
            implementation(projects.respectDatalayerDb)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.okhttp)
            implementation(libs.ktor.client.okhttp)


            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.content.negotiation)

            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.netty)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.server.conditional.headers)
        }
    }
}

android {
    namespace = "world.respect.datalayer.repository"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
