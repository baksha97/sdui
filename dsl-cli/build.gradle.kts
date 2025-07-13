import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinJvm)
    application
    kotlin("plugin.serialization") version "2.2.0"
}

group = "com.baksha97.sdui"
version = "1.0.0"
application {
    mainClass.set("com.baksha97.sdui.dsl.MainKt")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logback)
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.0")
    implementation(project(":shared-models"))
    testImplementation(libs.kotlin.testJunit)
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}