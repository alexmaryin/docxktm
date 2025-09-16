package io.github.alexmaryin.docxktm.parts

import io.github.alexmaryin.docxktm.dsl.DocxDsl
import io.github.alexmaryin.docxktm.models.WordImage
import jakarta.xml.bind.JAXBElement
import org.docx4j.XmlUtils
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.Document
import org.docx4j.wml.P
import org.docx4j.wml.Tbl
import org.mvel2.CompileException
import org.mvel2.MVEL.eval
import java.io.File

@DocxDsl
class Body(val document: WordprocessingMLPackage) : ContentProvider {

    private var imagesCount = 0L

    override fun <T : Any> add(element: T) {
        document.mainDocumentPart.content.add(element)
    }

    /**
     * returns a list of paragraphs from document
     */
    fun getParagraphs(): List<P> = document.mainDocumentPart.content.filterIsInstance<P>()

    /**
     * returns a list of tables from document
     */
    fun getTables(): List<Tbl> = document.mainDocumentPart.content.filterIsInstance<JAXBElement<Tbl>>().map { it.value }

    /**
     * binds an image from [filename] with [description] to the document
     * @param width width of the image in the document
     * @param height height of the image in the document
     */
    fun bindImage(filename: String, description: String = "", width: Long? = null, height: Long? = null): WordImage {
        val nextId = imagesCount + 1
        return WordImage(filename, description, nextId, width, height, File(filename).readBytes())
    }
}

