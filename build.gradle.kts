val ktorVersion = "2.0.3"

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation(platform("dev.schlaubi:stdx-bom:1.0.1"))
    implementation("dev.schlaubi:stdx-envconf")

    implementation(kotlin("reflect"))
}