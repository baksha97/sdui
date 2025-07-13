plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(libs.ksp.api)
    implementation(projects.sharedModels)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
