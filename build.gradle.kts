plugins {
    kotlin("jvm") version "2.2.10"
    id("org.jetbrains.dokka") version "1.8.20"
    id("maven-publish")
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "io.github.alexmaryin"
version = "1.2.0"

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
            version = "1.2.0"
        }
    }
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
java {
    withSourcesJar()
    withJavadocJar()
}