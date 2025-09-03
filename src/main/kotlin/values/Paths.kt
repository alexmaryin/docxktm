package io.github.alexmaryin.docxktm.values

import java.nio.file.Files
import java.nio.file.Path

internal object Paths {
    private fun createPathIfNotExistAndReturn(path: String): String {
        if (Files.notExists(Path.of(path))) {
            Files.createDirectories(Path.of(path))
        }
        return path
    }

    val TEST_IMAGES_DIR: String get() = createPathIfNotExistAndReturn("temp/images/")
    val TEST_DOCX_DIR: String get() = createPathIfNotExistAndReturn("temp/docxOut/")
    val TEMPLATES_DIR: String get() = createPathIfNotExistAndReturn("temp/templates/")
    val DOCX_DIR: String get() = createPathIfNotExistAndReturn("docxOut/")
    val TEMP_DIR: String get() = createPathIfNotExistAndReturn("docxOut/temp/")
}