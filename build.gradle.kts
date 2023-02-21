import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val imageio_version: String by project

plugins {
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.2"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "ru.kettuproj"
version = "0.0.1"
application {
    mainClass.set("ru.kettuproj.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")

    implementation("com.google.guava:guava:31.1-jre")

    implementation("org.apache.tika:tika-core:1.28.5")

    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("net.coobird:thumbnailator:0.4.19")
    implementation("com.twelvemonkeys.imageio:imageio-core:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-jpeg:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-bmp:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-hdr:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-icns:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-iff:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-pcx:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-pict:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-pnm:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-sgi:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-tga:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-tiff:$imageio_version")
    implementation("com.twelvemonkeys.imageio:imageio-xwd:$imageio_version")
    implementation(files("libs/imageio-webp-3.9.4.jar"))
}

tasks.withType<ShadowJar> {
    archiveFileName.set("CloudAlbum.jar")
}