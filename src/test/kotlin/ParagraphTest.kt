package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.*
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.models.TextStyle
import io.github.alexmaryin.docxktm.models.normalTextStyle
import io.github.alexmaryin.docxktm.parts.*
import io.github.alexmaryin.docxktm.values.Paths
import io.github.alexmaryin.docxktm.values.Styles
import io.github.alexmaryin.docxktm.values.WordColor
import io.github.alexmaryin.docxktm.values.defaultSpacing
import org.docx4j.wml.JcEnumeration
import org.docx4j.wml.UnderlineEnumeration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParagraphTest {
    @Test
    fun `Create 2-paragraph text with title in docx and verify it`() {
        DocxNew(Paths.TEST_DOCX_DIR + "test.docx") {
            body {
                paragraph(style = ParagraphStyle(styleName = Styles.TITLE)) {
                    text("Main title")
                }
                paragraph {
                    text(
                        text = "Body text for the first paragraph.",
                        style = TextStyle(
                            bold = true,
                            underlined = UnderlineEnumeration.SINGLE,
                            color = WordColor.Blue
                        )
                    )
                }
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "test.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 2 }
                assertEquals(expected = "Main title", actual = "${getParagraphs().first()}")
                assertEquals(expected = "Body text for the first paragraph.", actual = "${getParagraphs().last()}")
            }
        }
    }

    @Test
    fun `Create auto-p text with title in docx and verify it`() {
        DocxNew(Paths.TEST_DOCX_DIR + "test.docx") {
            body {
                paragraph(ParagraphStyle(styleName = Styles.TITLE)) {
                    text("Main title")
                }
                paragraph {
                    text(
                        text = "Body text for the first paragraph.",
                        style = TextStyle(
                            bold = true,
                            underlined = UnderlineEnumeration.SINGLE,
                            color = WordColor.Blue
                        )
                    )
                }
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "test.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 2 }
                assertEquals(expected = "Main title", actual = "${getParagraphs().first()}")
                assertEquals(expected = "Body text for the first paragraph.", actual = "${getParagraphs().last()}")
            }
        }
    }

    @Test
    fun `Create single paragraph with two-lines text`() {
        DocxNew(Paths.TEST_DOCX_DIR + "single_p.docx") {
            body {
                paragraph {
                    text("First line of the text.", breakLine = true)
                    text("Second line.")
                }
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "single_p.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 1 }
                assertEquals(expected = "First line of the text.Second line.", actual = "${getParagraphs().first()}")
            }
        }
    }

    @Test
    fun `Create document with paragraphs of different font size lines`() {
        DocxNew(Paths.TEST_DOCX_DIR + "font_size.docx") {
            body {
                repeat(10) {
                    val size = 36 - it * 2
                    paragraph {
                        text(
                            text = "Line with font of $size size",
                            style = normalTextStyle.copy(size = size)
                        )
                    }
                }
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "font_size.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 10 }
                getParagraphs().forEachIndexed { index, p ->
                    println(p)
                    with(p.runs()) {
                        assertTrue { size == 1 }
                        assertTrue { first().fontSize() == 36 - index * 2 }
                    }
                }
            }
        }
    }

    @Test
    fun `Create document with single paragraph and different sizes of text`() {
        DocxNew(Paths.TEST_DOCX_DIR + "font_size_single.docx") {
            body {
                paragraph {
                    repeat(10) {
                        val size = 10 + it * 2
                        text(
                            text = "Line with font of $size size",
                            style = normalTextStyle.copy(size = size),
                            breakLine = true
                        )
                    }
                }
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "font_size_single.docx", autoSave = false) {
            body {
                val p = getParagraphs()
                assertTrue { p.size == 1 }
                with(p.first().runs()) {
                    assertTrue { size == 10 }
                    forEachIndexed { index, run ->
                        assertTrue { run.fontSize() == 10 + index * 2 }
                    }
                }
            }
        }
    }

    @Test
    fun `Create document with paragraphs of different alignment`() {
        DocxNew(Paths.TEST_DOCX_DIR + "alignment.docx") {
            body {
                paragraph {
                    text("Left side alignment text")
                }
                paragraph(ParagraphStyle(alignment = JcEnumeration.RIGHT)) {
                    text("Right side alignment text")
                }
                paragraph(ParagraphStyle(alignment = JcEnumeration.BOTH)) {
                    text("Both sides alignment text")
                }
                paragraph(ParagraphStyle(alignment = JcEnumeration.CENTER)) {
                    text("Center alignment text")
                }
                paragraph(ParagraphStyle(alignment = JcEnumeration.DISTRIBUTE)) {
                    text("Distributed alignment text")
                }
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "alignment.docx", autoSave = false) {
            body {
                with(getParagraphs()) {
                    assertTrue { size == 5 }
                    val correctValues = listOf(
                        JcEnumeration.LEFT, JcEnumeration.RIGHT, JcEnumeration.BOTH,
                        JcEnumeration.CENTER, JcEnumeration.DISTRIBUTE
                    )
                    forEachIndexed { index, p ->
                        assertTrue { p.alignment() == correctValues[index] }
                    }
                }
            }
        }
    }

    @Test
    fun `Create document with different spacing in paragraph`() {
        DocxNew(Paths.TEST_DOCX_DIR + "spacing.docx") {
            body {
                paragraph(ParagraphStyle(spacing = defaultSpacing.copy(after = 24))) {
                    text(text = "First paragraph with spacing after 24 pt")
                }
                paragraph(ParagraphStyle(spacing = defaultSpacing.copy(before = 12))) {
                    text(text = "Second paragraph with spacing before 12 pt")
                }
                paragraph(ParagraphStyle(spacing = defaultSpacing.copy(between = 16))) {
                    text("Third paragraph with spacing between lines 16 pt", breakLine = true)
                    text("Third paragraph with spacing between lines 16 pt", breakLine = true)
                    text("Third paragraph with spacing between lines 16 pt")
                }
            }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "spacing.docx", autoSave = false) {
            body {
                val p = getParagraphs()
                assertTrue { p.size == 3 }
                assertEquals(expected = 24, actual = p[0].spacing().after)
                assertEquals(expected = 12, actual = p[1].spacing().before)
                assertEquals(expected = 16, actual = p[2].spacing().between)
            }
        }
    }
}