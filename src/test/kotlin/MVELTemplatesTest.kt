package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.values.Paths
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MVELTemplatesTest {

    private val simpleDict = hashMapOf(
        "name" to "Alex",
        "age" to "39",
        "date" to "01.01.2023",
        "undefined" to "undefined"
    )

    data class User(val name: String, val age: Int)

    private val richDict = mapOf(
        "user" to User("Alex", 39),
        "date" to LocalDate.of(2023, 1, 1),
        "active" to true
    )

    @Test
    fun `Simple docx template should return doc with merged fields`() {
        DocxOpen(Paths.TEMPLATES_DIR + "basic_template.docx",
            Paths.TEST_DOCX_DIR + "merged_result.docx") {
            body {
                mergeTemplate(simpleDict)
            }
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

    @Test
    fun `Rich template should return doc with merged fields`() {
        DocxOpen(Paths.TEMPLATES_DIR + "template2.docx",
            Paths.TEST_DOCX_DIR + "merged_result2.docx") {
            body {
                mergeRichTemplate(richDict)
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "merged_result2.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 5 }
                assertEquals(expected = "Second template test.", actual = "${getParagraphs()[0]}")
                assertEquals(expected = "Hello, my name is Alex and I’m 39.", actual = "${getParagraphs()[1]}")
                assertEquals(expected = "Today is 2023-01-01.", actual = "${getParagraphs()[2]}")
                assertEquals(expected = "User is Active.", actual = "${getParagraphs()[3]}")
            }
        }
    }
}