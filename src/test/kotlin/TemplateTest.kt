package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.templates.DocxTemplate
import io.github.alexmaryin.docxktm.templates.processTemplate
import io.github.alexmaryin.docxktm.values.CurrencyFormat
import io.github.alexmaryin.docxktm.values.DateFormat
import io.github.alexmaryin.docxktm.values.NumberFormat
import io.github.alexmaryin.docxktm.values.Paths
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DocxTemplateTest {

    private val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    @BeforeTest
    fun setup() {
        if (!File(Paths.TEST_DOCX_DIR + "template.docx").exists()) {
            DocxNew(Paths.TEST_DOCX_DIR + "template.docx") {
                body {
                    paragraph {
                        text("This template is made by \${name} for testing \${library} library.")
                    }
                    paragraph {
                        text("Today is: \${today}.")
                    }
                }
            }
        }
        if (!File(Paths.TEST_DOCX_DIR + "format_template.docx").exists()) {
            DocxNew(Paths.TEST_DOCX_DIR + "format_template.docx") {
                body {
                    paragraph {
                        text("Simple string \${str} is simple string.")
                        text("Now number value \${number} with two digits after decimal separator.")
                        text("Now number value \${numberDef} with default toString.")
                        text("Then rubles amount \${amount} for the price.")
                        text("Finally, today date is \${today} with format MMMM dd, yyyy.")
                    }
                }
            }
        }
    }

    @Test
    fun `Open docx with placeholders and process with replacements map`() {
        DocxOpen(
            Paths.TEST_DOCX_DIR + "template.docx",
            Paths.TEST_DOCX_DIR + "output.docx"
        ) {
            val replacements = mapOf(
                "today" to today,
                "name" to "Alex Maryin",
                "library" to "DocxKtm"
            )
            processTemplate(replacements)
        }

        DocxOpen(Paths.TEST_DOCX_DIR + "output.docx", autoSave = false) {
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
    fun `Create placeholders with DSL test`() {
        DocxTemplate(
            templateFilename = Paths.TEST_DOCX_DIR + "template.docx",
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
            templateFilename = Paths.TEST_DOCX_DIR + "format_template.docx",
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
}