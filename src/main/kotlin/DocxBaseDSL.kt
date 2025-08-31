package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.parts.DEFAULT_FOOTER
import io.github.alexmaryin.docxktm.parts.DEFAULT_HEADER
import io.github.alexmaryin.docxktm.values.PageSize
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

/**
 * Creates new docx file from scratch
 * @param filename creates file with filename including directories
 * @param autoSave default True
 * @param pageSize type of the page from [PageSizePaper] default A4
 * @param landscape default False
 * @param block lambda for document populating
 */
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

/**
 * Opens existing docx file
 * @param filename creates file with filename including directories
 * @param newFilename filename for saving the changes
 * @param autoSave default True
 * @param block lambda for document editing
 */
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
        val newFile = File(newFilename ?: filename)
        if (autoSave) try {
            save(newFile)
        } catch (e: Docx4JException) {
            println("ERROR while trying to save docx file ${newFile.name}")
            throw e
        }
    }
}

/**
 * @return document orientation portrait or landscape
 */
fun WordprocessingMLPackage.orientation(): STPageOrientation = mainDocumentPart.jaxbElement.body.sectPr.pgSz.orient

/**
 * @return page size
 */
fun WordprocessingMLPackage.pageSize(): PageSize {
    val size = mainDocumentPart.jaxbElement.body.sectPr.pgSz
    return when {
        PageSize.Letter.isEqual(size) -> PageSize.Letter
        PageSize.A3.isEqual(size) -> PageSize.A3
        PageSize.A4.isEqual(size) -> PageSize.A4
        PageSize.A5.isEqual(size) -> PageSize.A5
        else -> PageSize.Custom(size.w.toInt(), size.h.toInt())
    }
}

/**
 * @return header content as a list of paragraphs
 */
fun WordprocessingMLPackage.getHeaderContent(name: String = DEFAULT_HEADER): List<P> {
    val header = parts[PartName(name)]
    return if (header is HeaderPart) header.content.filterIsInstance<P>()
    else emptyList()
}

/**
 * @return footer content as a list of paragraphs
 */
fun WordprocessingMLPackage.getFooterContent(name: String = DEFAULT_FOOTER): List<P> {
    val footer = parts[PartName(name)]
    return if (footer is FooterPart) footer.content.filterIsInstance<P>()
    else emptyList()
}


