plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.dokka") version "1.8.20"
    id("maven-publish")
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "io.github.alexmaryin"
version = "1.0.2"

dependencies {
    api("org.docx4j:docx4j-JAXB-ReferenceImpl:11.5.4")
    implementation("org.mvel:mvel2:2.5.0.Final")

    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "io.github.alexmaryin"
            artifactId = "docxktm"
            version = "1.0.3"
        }
    }
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}

kotlin {
    jvmToolchain(17)
}
java {
    withSourcesJar()
    withJavadocJar()
}