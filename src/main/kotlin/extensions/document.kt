package io.github.alexmaryin.docxktm.extensions

import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.parts.Body
import io.github.alexmaryin.docxktm.parts.Footer
import io.github.alexmaryin.docxktm.parts.Header
import io.github.alexmaryin.docxktm.parts.Paragraph
import org.docx4j.openpackaging.packages.WordprocessingMLPackage

/**
 * The main block for docx populating and editing.
 * Gets the document package as a context receiver
 */
fun WordprocessingMLPackage.body(block: Body.() -> Unit) {
    with(Body(this)) {
        block(this)
    }
}

/**
 * The block for header editing
 * @param style [ParagraphStyle] style for text in header
 */
fun WordprocessingMLPackage.header(style: ParagraphStyle = ParagraphStyle(), block: Paragraph.() -> Unit) {
    val header = Header(this)
    Paragraph(style).apply {
        block(this)
        header.putContent(getP())
    }
}

/**
 * The block for footer editing
 * @param style [ParagraphStyle] style for text in footer
 */
fun WordprocessingMLPackage.footer(style: ParagraphStyle = ParagraphStyle(), block: Paragraph.() -> Unit) {
    val footer = Footer(this)
    Paragraph(style).apply {
        block(this)
        footer.putContent(getP())
    }
}