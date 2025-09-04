package io.github.alexmaryin.docxktm.parts

import io.github.alexmaryin.docxktm.docxFactory
import io.github.alexmaryin.docxktm.dsl.DocxDsl
import io.github.alexmaryin.docxktm.extensions.substringBetween
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.models.TextStyle
import io.github.alexmaryin.docxktm.models.footerTextStyle
import io.github.alexmaryin.docxktm.values.ptToTwips
import org.docx4j.wml.CTSimpleField
import org.docx4j.wml.P
import java.math.BigInteger

class Paragraph(style: ParagraphStyle?) : ContentProvider, ParagraphContent {
    private val paragraph = docxFactory.createP()

    override fun <T : Any> add(element: T) {
        paragraph.content.add(element)
    }

    fun getP(): P = paragraph

    init {
        style?.let {
            val paragraphProperties = docxFactory.createPPr()
            paragraphProperties.pStyle = docxFactory.createPPrBasePStyle().apply { `val` = it.styleName }
            paragraphProperties.jc = docxFactory.createJc().apply { `val` = it.alignment }
            paragraphProperties.spacing = docxFactory.createPPrBaseSpacing().apply {
                it.spacing.after?.let { after = BigInteger.valueOf(it.ptToTwips()) }
                it.spacing.before?.let { before = BigInteger.valueOf(it.ptToTwips()) }
                it.spacing.between?.let { line = BigInteger.valueOf(it.ptToTwips()) }
            }
            paragraph.pPr = paragraphProperties
        }
    }

    /**
     * puts page number with specified [template] and [style] inside the paragraph
     */
    fun pageNumber(template: String? = null, style: TextStyle = footerTextStyle) {

        template?.let { text(it.substringBefore("#p"), style, breakLine = false) }

        val page = docxFactory.createPFldSimple(CTSimpleField().apply { instr = "PAGE \\* MERGEFORMAT" })
        paragraph.content.add(page)

        template?.let {
            text(it.substringBetween("#p", "#t"), style, breakLine = false)
            if (it.contains("#t")) {
                val total = docxFactory.createPFldSimple(
                    CTSimpleField().apply { instr = "NUMPAGES \\* MERGEFORMAT" }
                )
                paragraph.content.add(total)
            }
            text(it.substringAfter("#t"), style, breakLine = false)
        }
    }
}



