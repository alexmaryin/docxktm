package io.github.alexmaryin.docxktm.parts

import org.docx4j.jaxb.Context
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart
import org.docx4j.wml.Ftr
import org.docx4j.wml.HdrFtrRef
import org.docx4j.wml.P

const val DEFAULT_FOOTER = "/word/footer.xml"

class Footer(
    document: WordprocessingMLPackage,
    name: String? = null
) {
    private val ftr: Ftr
    private val footer: FooterPart = FooterPart(PartName(name ?: DEFAULT_FOOTER))

    init {
        document.parts.put(footer)
        ftr = Context.getWmlObjectFactory().createFtr()
        footer.jaxbElement = ftr
        val relation = document.mainDocumentPart.addTargetPart(footer)
        val section = document.documentModel.sections.last()?.sectPr ?: io.github.alexmaryin.docxktm.docxFactory.createSectPr()
        io.github.alexmaryin.docxktm.docxFactory.createFooterReference().apply {
            id = relation.id
            type = HdrFtrRef.DEFAULT
            section.egHdrFtrReferences.add(this)
        }
    }

    fun putContent(paragraph: P) {
        ftr.content.add(paragraph)
    }
}