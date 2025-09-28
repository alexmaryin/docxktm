package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.parts.pageDivider
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.values.Paths
import org.docx4j.wml.Br
import org.docx4j.wml.STBrType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PageDividerTest {
    @Test
    fun `pageDivider should add divider and new page into the document`() {
        DocxNew(Paths.TEST_DOCX_DIR + "page_divider.docx") {
            body {
                paragraph { text("First page") }
                pageDivider()
                paragraph { text("Second page") }
            }
            DocxOpen(Paths.TEST_DOCX_DIR + "page_divider.docx", autoSave = false) {
                body {
                    val breaks = mainDocumentPart.getJAXBNodesViaXPath("//w:br", true)
                    assertFalse { breaks.isEmpty() }
                    assertTrue { (breaks.first() as Br).type == STBrType.PAGE }
                }
            }
        }
    }
}