plugins {
    kotlin("jvm") version "1.9.0"
    id("convention.publication")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.alexmaryin"
            artifactId = "docxktm"
            version = "1.0.1"

            from(components["java"])
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("org.docx4j:docx4j-JAXB-ReferenceImpl:11.4.9")
    implementation("org.mvel:mvel2:2.5.0.Final")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}

kotlin {
    jvmToolchain(11)
}