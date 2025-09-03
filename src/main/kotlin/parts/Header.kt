package io.github.alexmaryin.docxktm.parts

import io.github.alexmaryin.docxktm.dsl.DocxDsl
import org.docx4j.jaxb.Context
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart
import org.docx4j.wml.Hdr
import org.docx4j.wml.HdrFtrRef
import org.docx4j.wml.P

const val DEFAULT_HEADER = "/word/header.xml"

@DocxDsl
class Header(
    document: WordprocessingMLPackage,
    name: String? = null
) {
    private val hdr: Hdr
    private val header: HeaderPart = HeaderPart(PartName(name ?: DEFAULT_HEADER))

    init {
        document.parts.put(header)
        hdr = Context.getWmlObjectFactory().createHdr()
        header.jaxbElement = hdr
        val relation = document.mainDocumentPart.addTargetPart(header)
        val section = document.documentModel.sections.last()?.sectPr ?:
            io.github.alexmaryin.docxktm.docxFactory.createSectPr()
        io.github.alexmaryin.docxktm.docxFactory.createHeaderReference().apply {
            id = relation.id
            type = HdrFtrRef.DEFAULT
            section.egHdrFtrReferences.add(this)
        }
    }

    fun putContent(paragraph: P) {
        hdr.content.add(paragraph)
    }
}
