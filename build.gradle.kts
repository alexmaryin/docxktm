plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.serialization") version ("2.2.10")
    id("com.vanniktech.maven.publish") version "0.34.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "io.github.alexmaryin"
version = "1.3.3"

dependencies {
    api("org.docx4j:docx4j-JAXB-ReferenceImpl:11.5.5")
    implementation("org.mvel:mvel2:2.5.0.Final")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    testImplementation(kotlin("test"))
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(group.toString(), "docxktm", version.toString())
    pom {
        name = "DocxKtm"
        description = "Create, open edit docx files, populate templates with intuitive DSL"
        inceptionYear = "2025"
        url = "https://github.com/alexmaryin/docxktm"
        licenses {
            license {
                name = "Apache 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0"
            }
        }
        developers {
            developer {
                id = "alexmaryin"
                name = "Alex Maryin"
                email = "java.ul@gmail.com"
            }
        }
        scm {
            url = "https://github.com/alexmaryin/docxktm"
        }
        properties.put("kotlin.minimum.version", "2.2.10")
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
}