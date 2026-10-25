plugins {
    kotlin("jvm") version "2.4.20"
    application
    id("com.gradleup.shadow") version "9.6.1"
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
    // JLayer music player
    // Source: https://mvnrepository.com/artifact/javazoom/jlayer
    implementation("javazoom:jlayer:1.0.1")
}

kotlin {
    jvmToolchain(25)
}

application {
    mainClass.set("dev.naspo.fortnitelobbymusicplayer.MainKt")
}


tasks {
    test {
        useJUnitPlatform()
    }

    // Disabling the jar task (and all other related output tasks from the application plugin)
    // as we only want the fat jar output from the shadowJar task.
    jar { enabled = false }
    startScripts { enabled = false }
    distZip { enabled = false }
    distTar { enabled = false }

    shadowJar {
        // Removing the "-all" suffix from the end of the build output.
        archiveClassifier.set("")
    }

    build {
        // Force standard build task to run shadowJar
        dependsOn(shadowJar)
    }
}