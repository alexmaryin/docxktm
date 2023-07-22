package io.github.alexmaryin.docxktm.extensions

import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.parts.Body
import io.github.alexmaryin.docxktm.parts.Footer
import io.github.alexmaryin.docxktm.parts.Header
import io.github.alexmaryin.docxktm.parts.Paragraph
import org.docx4j.openpackaging.packages.WordprocessingMLPackage

fun WordprocessingMLPackage.body(block: Body.() -> Unit) {
    with(Body(this)) {
        block(this)
    }
}

fun WordprocessingMLPackage.header(style: ParagraphStyle = ParagraphStyle(), block: Paragraph.() -> Unit) {
    val header = Header(this)
    Paragraph(style).apply {
        block(this)
        header.putContent(getP())
    }
}

fun WordprocessingMLPackage.footer(style: ParagraphStyle = ParagraphStyle(), block: Paragraph.() -> Unit) {
    val footer = Footer(this)
    Paragraph(style).apply {
        block(this)
        footer.putContent(getP())
    }
}