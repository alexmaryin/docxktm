package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.extensions.footer
import io.github.alexmaryin.docxktm.extensions.header
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.models.normalTextStyle
import io.github.alexmaryin.docxktm.parts.DEFAULT_FOOTER
import io.github.alexmaryin.docxktm.parts.DEFAULT_HEADER
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.values.Alignment
import io.github.alexmaryin.docxktm.values.Paths
import io.github.alexmaryin.docxktm.values.UnderlineStyle
import kotlin.test.Test
import kotlin.test.assertEquals

class HeaderFooterTest {

    @Test
    fun `Create simplest doc with header and footer`() {
        DocxNew(Paths.TEST_DOCX_DIR +"main_parts.docx") {
            header { text("Header of document") }
            body {
                paragraph { text("First block of main text.") }
            }
            footer {
                text("Footer of document")
                pageNumber()
            }
        }
    }

    @Test
    fun `Create docx with header and footer`() {
        DocxNew(Paths.TEST_DOCX_DIR + "header.docx") {
            header(ParagraphStyle(alignment = Alignment.CENTER)) {
                text("Header of document", style = normalTextStyle.copy(italic = true))
            }
            body {
                paragraph {
                    text("First block of main text.")
                }
            }
            footer(ParagraphStyle(alignment = Alignment.RIGHT)) {
                text("Footer of document", style = normalTextStyle.copy(underlined = UnderlineStyle.DASH))
                pageNumber()
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "header.docx", autoSave = false) {
            val header = getHeaderContent(DEFAULT_HEADER)
            assertEquals(expected = "Header of document", actual = "${header.first()}")
            val footer = getFooterContent(DEFAULT_FOOTER)
            assertEquals(expected = "Footer of document", actual = "${footer.first()}")
        }
    }

    @Test
    fun `Create docx with footer and page number`() {
        DocxNew(Paths.TEST_DOCX_DIR + "page_number.docx") {
            body {
                paragraph {
                    text("Simple text in the document.")
                }
            }
            footer(ParagraphStyle(alignment = Alignment.CENTER)) {
                pageNumber("page #p of #t")
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "page_number.docx", autoSave = false) {
            val footer = getFooterContent(DEFAULT_FOOTER)
            assertEquals(expected = "page  of ", actual = "${footer.first()}")
        }
    }
}