plugins {
    kotlin("jvm") version "2.3.20"
}

group = "com.gigapi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    implementation("com.badlogicgames.gdx:gdx-freetype:1.14.2")
    implementation("com.badlogicgames.gdx:gdx:1.14.2")

    implementation("net.onedaybeard.artemis:artemis-odb:2.3.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.2")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}