package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.values.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DocxTemplateTest {

    @Test
    fun `Open docx with placeholders and process with replacements map`() {

        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

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
                val today =
                assertEquals(
                    expected = "Today is: $today.",
                    actual = "${getParagraphs().last()}"
                )
            }
        }
    }
}