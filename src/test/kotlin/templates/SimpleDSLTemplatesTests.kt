package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxOpen
import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.values.CurrencyFormat
import io.github.alexmaryin.docxktm.values.DateFormat
import io.github.alexmaryin.docxktm.values.NumberFormat
import io.github.alexmaryin.docxktm.values.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class SimpleDSLTemplatesTests : TemplatesTestBase() {
    @Test
    fun `Rich template should return doc with merged fields`() {
        DocxTemplate(
            Paths.TEMPLATES_DIR + "template_rich.docx",
            Paths.TEST_DOCX_DIR + "merged_result2.docx"
        ) {
            fromMap(richDict)
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "merged_result2.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 5 }
                assertEquals(expected = "Second template test.", actual = "${getParagraphs()[0]}")
                assertEquals(expected = "Hello, my name is Alex and I’m 39.", actual = "${getParagraphs()[1]}")
                assertEquals(expected = "Today is 2023-01-01.", actual = "${getParagraphs()[2]}")
                assertEquals(expected = "User is Active.", actual = "${getParagraphs()[3]}")
                assertEquals(expected = "Paragraph with error: ", actual = "${getParagraphs()[4]}")
            }
        }
    }

    @Test
    fun `Create placeholders with DSL test`() {
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        DocxTemplate(
            templateFilename = Paths.TEMPLATES_DIR + "template.docx",
            outputFilename = Paths.TEST_DOCX_DIR + "outputDSL.docx"
        ) {
            "today" to today
            "name" to "Alex Maryin"
            "library" to "DocxKtm"
        }

        DocxOpen(Paths.TEST_DOCX_DIR + "outputDSL.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 2 }
                assertEquals(
                    expected = "This template is made by Alex Maryin for testing DocxKtm library.",
                    actual = "${getParagraphs().first()}"
                )
                assertEquals(
                    expected = "Today is: $today.",
                    actual = "${getParagraphs().last()}"
                )
            }
        }
    }

    @Test
    fun `Create different formats for placeholders`() {
        DocxTemplate(
            templateFilename = Paths.TEMPLATES_DIR + "format_template.docx",
            outputFilename = Paths.TEST_DOCX_DIR + "format_output.docx"
        ) {
            "str" to "simple string"
            "number" to 3.1432 with NumberFormat("#,##0.00")
            "numberDef" to 123.456
            "amount" to 3555.0 with CurrencyFormat.RUB
            "today" to LocalDateTime.now() with DateFormat("MMMM dd, yyyy")
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "format_output.docx", autoSave = false) {
            body {
                val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                assertEquals(
                    expected = "Simple string simple string is simple string." +
                            "Now number value 3,14 with two digits after decimal separator." +
                            "Now number value 123,456 with default toString." +
                            "Then rubles amount 3 555,00 ₽ for the price." +
                            "Finally, today date is $date with format MMMM dd, yyyy.",
                    actual = "${getParagraphs().first()}"
                )
            }
        }
    }

    @Test
    fun `Create different formats for placeholders with default values`() {
        DocxTemplate(
            templateFilename = Paths.TEMPLATES_DIR + "default_filler_template.docx",
            outputFilename = Paths.TEST_DOCX_DIR + "default_filler_output.docx"
        ) {
            "name" to "Alex"
            "place" to "Moscow"
            "age" to 41
            "status" to "employed"
            "unknown" to "this filed should not be replaced"
        }

        DocxOpen(Paths.TEST_DOCX_DIR + "default_filler_output.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 3 }
                assertEquals(
                    expected = "Hello Alex, welcome to Moscow.",
                    actual = "${getParagraphs()[0]}"
                )
                assertEquals(
                    expected = "Your age is 41 and status is employed.",
                    actual = "${getParagraphs()[1]}"
                )
                assertEquals(
                    expected = "Missing field: .",
                    actual = "${getParagraphs()[2]}"
                )
            }
        }
    }

    @Test
    fun `Create custom data class placeholder with evaluation fields and filler`() {
        val testUser = User("Alex", 41, Gender.MALE)

        DocxTemplate(
            templateFilename = Paths.TEMPLATES_DIR + "klass_template.docx",
            outputFilename = Paths.TEST_DOCX_DIR + "klass_output.docx",
            filler = "no data"
        ) {
            "user" to testUser
            "simpleField" to "text"
        }
        /** OUTPUT:
         * Hello Alex, welcome to MVEL2.
         * Your age is 41 and gender is male.
         * Simple field: text.
         * Missing field: no data.
         */
        DocxOpen(Paths.TEST_DOCX_DIR + "klass_output.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 4 }
                assertEquals(
                    expected = "Hello Alex, welcome to MVEL2.",
                    actual = "${getParagraphs()[0]}"
                )
                assertEquals(
                    expected = "Your age is 41 and gender is male.",
                    actual = "${getParagraphs()[1]}"
                )
                assertEquals(
                    expected = "Simple field: text.",
                    actual = "${getParagraphs()[2]}"
                )
                assertEquals(
                    expected = "Missing field: no data.",
                    actual = "${getParagraphs()[3]}"
                )
            }
        }
    }
}