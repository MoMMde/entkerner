val ktorVersion = "2.0.3"

plugins {
    java
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.20"

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

    implementation("de.swiesend:secret-service:1.8.1-jdk17")

    implementation("de.rtner:PBKDF2:1.1.4")

    implementation("com.h2database:h2:2.1.214")
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")
    implementation("org.jetbrains.exposed", "exposed-core", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")

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
