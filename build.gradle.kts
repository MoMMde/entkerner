val ktorVersion = "2.0.3"

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"

    id("com.github.johnrengelman.shadow") version "7.1.2"

    application
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

application {
    mainClass.set("net.kerner.entkerner.EntkernerKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "net.kerner.entkerner.EntkernerKt"
    }
}