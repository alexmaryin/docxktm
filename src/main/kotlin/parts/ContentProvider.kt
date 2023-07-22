package io.github.alexmaryin.docxktm.parts

import io.github.alexmaryin.docxktm.extensions.applyProperties
import io.github.alexmaryin.docxktm.models.*
import io.github.alexmaryin.docxktm.parts.tables.Table
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage

interface ContentProvider {
    fun <T : Any> add(element: T)
}

interface ParagraphContent {
    fun <T : Any> add(element: T)
}

fun ContentProvider.paragraph(
    style: ParagraphStyle? = null,
    block: ParagraphContent.() -> Unit
) {
    Paragraph(style).apply {
        block(this)
        this@paragraph.add(getP())
    }
}

fun ParagraphContent.text(text: String, style: TextStyle = normalTextStyle, breakLine: Boolean = false) {
    val runBlock = io.github.alexmaryin.docxktm.docxFactory.createR()
    val textBlock = io.github.alexmaryin.docxktm.docxFactory.createText()

    textBlock.value = text
    runBlock.content.add(textBlock)
    runBlock.rPr = io.github.alexmaryin.docxktm.docxFactory.createRPr().applyProperties(style)
    if (breakLine) runBlock.content.add(io.github.alexmaryin.docxktm.docxFactory.createBr())

    add(runBlock)
}

fun ContentProvider.table(
    style: TableStyle? = null,
    block: Table.() -> Unit
) {
    Table(style).apply {
        block(this)
        this@table.add(getTbl())
    }
    paragraph {}    // this is caused by MS Word bug: you need an empty paragraph after table to correct schema
}

context(Body)
fun ParagraphContent.imageFromFile(file: WordImage) {
    val runBlock = io.github.alexmaryin.docxktm.docxFactory.createR()
    val imagePart = BinaryPartAbstractImage.createImagePart(document, file.bytes)
    val inline = when {
        file.height != null && file.width != null -> imagePart.createImageInline(
            file.filename,
            file.description,
            file.id,
            file.id.toInt(),
            file.width,
            file.height,
            false
        )

        file.width != null -> imagePart.createImageInline(
            file.filename,
            file.description,
            file.id,
            file.id.toInt(),
            file.width,
            false
        )

        else -> imagePart.createImageInline(
            file.filename,
            file.description,
            file.id,
            file.id.toInt(),
            false
        )
    }

    val drawing = io.github.alexmaryin.docxktm.docxFactory.createDrawing()
    drawing.anchorOrInline.add(inline)
    runBlock.content.add(drawing)
    add(runBlock)
}
