package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxOpen
import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.values.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class SimpleMergeTests : TemplatesTestBase() {
    @Test
    fun `Simple docx template should return doc with merged fields`() {
        DocxOpen(
            Paths.TEMPLATES_DIR + "basic_template.docx",
            Paths.TEST_DOCX_DIR + "merged_result.docx"
        ) {
            body { mergeTemplateStrMap(simpleDict) }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "merged_result.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 3 }
                assertEquals(expected = "First template test.", actual = "${getParagraphs()[0]}")
                assertEquals(expected = "Hello, my name is Alex and I’m 39.", actual = "${getParagraphs()[1]}")
                assertEquals(expected = "Today is 01.01.2023.", actual = "${getParagraphs()[2]}")
            }
        }
    }
}