package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.templates.DocxTemplate
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

    data class User(
        val name: String,
        val age: Int,
        val gender: Gender
    )

    enum class Gender { MALE }

    private val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    private val testUser = User("Alex", 41, Gender.MALE)

    @BeforeTest
    fun setup() {
        if (!File(Paths.TEMPLATES_DIR + "template.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "template.docx") {
                body {
                    paragraph {
                        text($$"This template is made by ${name} for testing ${library} library.")
                    }
                    paragraph {
                        text($$"Today is: ${today}.")
                    }
                }
            }
        }
        if (!File(Paths.TEMPLATES_DIR + "default_filler_template.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "default_filler_template.docx") {
                body {
                    paragraph {
                        text($$"Hello ${name}, welcome to ${place}.")
                    }
                    paragraph {
                        text($$"Your age is ${age} and status is ${status}.")
                    }
                    paragraph {
                        text($$"Missing field: ${missing}.")
                    }
                }
            }
        }
        if (!File(Paths.TEMPLATES_DIR + "klass_template.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "klass_template.docx") {
                body {
                    paragraph {
                        text($$"Hello ${user.name}, welcome to MVEL2.")
                    }
                    paragraph {
                        text($$"Your age is ${user.age} and gender is ${user.gender.name() == 'MALE' ? 'male' : 'female'}.")
                    }
                    paragraph {
                        text($$"Simple field: ${simpleField}.")
                    }
                    paragraph {
                        text($$"Missing field: ${missing}.")
                    }
                }
            }
        }
    }

    @Test
    fun `Create placeholders with DSL test`() {
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
