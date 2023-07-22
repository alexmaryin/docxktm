package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.parts.DEFAULT_FOOTER
import io.github.alexmaryin.docxktm.parts.DEFAULT_HEADER
import io.github.alexmaryin.docxktm.values.PageSize
import io.github.alexmaryin.docxktm.values.Paths
import org.docx4j.jaxb.Context
import org.docx4j.model.structure.PageSizePaper
import org.docx4j.openpackaging.exceptions.Docx4JException
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart
import org.docx4j.wml.ObjectFactory
import org.docx4j.wml.P
import org.docx4j.wml.STPageOrientation
import java.io.File

val docxFactory: ObjectFactory = Context.getWmlObjectFactory()

fun DocxNew(
    filename: String,
    autoSave: Boolean = true,
    pageSize: PageSizePaper = PageSizePaper.A4,
    landscape: Boolean = false,
    block: WordprocessingMLPackage.() -> Unit
) {
    with(WordprocessingMLPackage.createPackage(pageSize, landscape)) {
        block(this)
        val file = File(filename)
        if (autoSave) try {
            save(file)
        } catch (e: Docx4JException) {
            println("ERROR while trying to save docx file $filename")
            throw e
        }
    }
}

fun DocxOpen(
    filename: String,
    newFilename: String? = null,
    autoSave: Boolean = true,
    block: WordprocessingMLPackage.() -> Unit
) {
    val file = File(filename)
    check(file.isFile) {
        println("File $filename is not exists")
    }
    with(WordprocessingMLPackage.load(file)) {
        block(this)
        val newFile = File(Paths.TEST_DOCX_DIR + (newFilename ?: filename))
        if (autoSave) try {
            save(newFile)
        } catch (e: Docx4JException) {
            println("ERROR while trying to save docx file ${newFile.name}")
            throw e
        }
    }
}

fun WordprocessingMLPackage.orientation(): STPageOrientation = mainDocumentPart.jaxbElement.body.sectPr.pgSz.orient

fun WordprocessingMLPackage.pageSize(): PageSize {
    val size = mainDocumentPart.jaxbElement.body.sectPr.pgSz
    return when {
        PageSize.Letter.isEqual(size) -> PageSize.Letter
        PageSize.A3.isEqual(size) -> PageSize.Letter
        PageSize.A4.isEqual(size) -> PageSize.Letter
        PageSize.A5.isEqual(size) -> PageSize.Letter
        else -> PageSize.Custom(size.w.toInt(), size.h.toInt())
    }
}

fun WordprocessingMLPackage.getHeaderContent(name: String = DEFAULT_HEADER): List<P> {
    val header = parts[PartName(name)]
    return if (header is HeaderPart) header.content.filterIsInstance<P>()
    else emptyList()
}

fun WordprocessingMLPackage.getFooterContent(name: String = DEFAULT_FOOTER): List<P> {
    val footer = parts[PartName(name)]
    return if (footer is FooterPart) footer.content.filterIsInstance<P>()
    else emptyList()
}

