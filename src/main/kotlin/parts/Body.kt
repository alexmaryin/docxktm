package io.github.alexmaryin.docxktm.parts

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

    /**
     * Populates docx template with fields from string dictionary [HashMap] <br>
     * Field for population should be bounded with ```${field_name}``` symbols
     */
    fun mergeTemplate(dict: HashMap<String, String>) {
        document.mainDocumentPart.variableReplace(dict)
    }

    /**
     * Populates docx template with fields from dictionary of any types including
     * data classes of Kotlin, any Java classes, and expressions with MVEL2 syntax.
     * At the moment, only single fields, properties, methods and ternary operator are supported.
     */
    fun mergeRichTemplate(dict: Map<String, Any>) {
        val jc = document.mainDocumentPart.jaxbContext
        val templateString = XmlUtils.marshaltoString(document.mainDocumentPart.jaxbElement, true, false, jc)
        val mergedString = templateString.replace(Regex("\\$\\{(.*?)}")) {
            println("match ${it.value}")
            val expression = it.groups[1]?.value?.replace(Regex("<[^>]*>"), "")
            println("cleared expression $expression")
            val value = try {
                eval(expression, dict).toString()
            } catch (e: CompileException) { "" }
            println("evaluated $value")
            value
        }

        document.mainDocumentPart.jaxbElement = XmlUtils.unwrap(XmlUtils.unmarshalString(mergedString, jc)) as Document
    }
}

