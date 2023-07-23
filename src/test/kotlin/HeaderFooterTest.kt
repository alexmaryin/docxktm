package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.extensions.footer
import io.github.alexmaryin.docxktm.extensions.header
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.models.normalTextStyle
import io.github.alexmaryin.docxktm.parts.*
import io.github.alexmaryin.docxktm.values.Paths
import org.docx4j.wml.JcEnumeration
import org.docx4j.wml.UnderlineEnumeration
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
            header(ParagraphStyle(alignment = JcEnumeration.CENTER)) {
                text("Header of document", style = normalTextStyle.copy(italic = true))
            }
            body {
                paragraph {
                    text("First block of main text.")
                }
            }
            footer(ParagraphStyle(alignment = JcEnumeration.RIGHT)) {
                text("Footer of document", style = normalTextStyle.copy(underlined = UnderlineEnumeration.DASH))
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
            footer(ParagraphStyle(alignment = JcEnumeration.CENTER)) {
                pageNumber("page #p of #t")
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "page_number.docx", autoSave = false) {
            val footer = getFooterContent(DEFAULT_FOOTER)
            assertEquals(expected = "page  of ", actual = "${footer.first()}")
        }
    }
}