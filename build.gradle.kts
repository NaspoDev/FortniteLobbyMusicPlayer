plugins {
    kotlin("jvm") version "2.4.20"
}

group = "dev.naspo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Tesseract OCR via Tess4J
    // Source: https://mvnrepository.com/artifact/net.sourceforge.tess4j/tess4j
    implementation("net.sourceforge.tess4j:tess4j:5.20.0")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}