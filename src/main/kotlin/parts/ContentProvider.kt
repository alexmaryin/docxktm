package io.github.alexmaryin.docxktm.parts

import io.github.alexmaryin.docxktm.docxFactory
import io.github.alexmaryin.docxktm.extensions.applyProperties
import io.github.alexmaryin.docxktm.models.*
import io.github.alexmaryin.docxktm.parts.tables.Table
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage
import org.docx4j.wml.STBrType
import javax.imageio.ImageIO

interface ContentProvider {
    fun <T : Any> add(element: T)
}

interface ParagraphContent {
    fun <T : Any> add(element: T)
}

/**
 * main block making a new paragraph in the document
 */
fun ContentProvider.paragraph(
    style: ParagraphStyle? = null,
    block: ParagraphContent.() -> Unit
) {
    Paragraph(style).apply {
        block(this)
        this@paragraph.add(getP())
    }
}

/**
 * main block making a new text run inside the paragraph
 */
fun ParagraphContent.text(text: String, style: TextStyle = normalTextStyle, breakLine: Boolean = false) {
    val runBlock = docxFactory.createR()
    val textBlock = docxFactory.createText()

    textBlock.value = text
    runBlock.content.add(textBlock)
    runBlock.rPr = docxFactory.createRPr().applyProperties(style)
    if (breakLine) runBlock.content.add(docxFactory.createBr())

    add(runBlock)
}

/**
 * main block making a new table in the document
 */
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

/**
 * main block putting a new image in the content block
 */
context(body: Body)
fun ParagraphContent.imageFromFile(file: WordImage) {
    val runBlock = docxFactory.createR()
    val imagePart = BinaryPartAbstractImage.createImagePart(body.document, file.bytes)
    val info = ImageIO.read(file.bytes.inputStream())
    val originWidthPx = info.width
    val originHeightPx = info.height

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

        file.width != null -> {
            val ratio = originHeightPx.toDouble() / originWidthPx.toDouble()
            val scaledHeight = (file.width * ratio).toLong()
            imagePart.createImageInline(
                file.filename,
                file.description,
                file.id,
                file.id.toInt(),
                file.width,
                scaledHeight,
                false
            )
        }

        file.height != null -> {
            val ratio = originWidthPx.toDouble() / originHeightPx.toDouble()
            val scaledWidth = (file.height * ratio).toLong()
            imagePart.createImageInline(
                file.filename,
                file.description,
                file.id,
                file.id.toInt(),
                scaledWidth,
                file.height,
                false
            )
        }

        else -> imagePart.createImageInline(
            file.filename,
            file.description,
            file.id,
            file.id.toInt(),
            originWidthPx.fromPxToEMU(),
            originHeightPx.fromPxToEMU(),
            false
        )
    }

    val drawing = docxFactory.createDrawing()
    drawing.anchorOrInline.add(inline)
    runBlock.content.add(drawing)
    add(runBlock)
}

/**
 * Add page divider into the document
 */
fun ContentProvider.pageDivider() {
    paragraph {
        val runBlock = docxFactory.createR()
        runBlock.content.add(docxFactory.createBr().apply {
            type = STBrType.PAGE
        })
        add(runBlock)
    }
}
