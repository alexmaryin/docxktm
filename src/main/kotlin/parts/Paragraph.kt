package io.github.alexmaryin.docxktm.parts

import io.github.alexmaryin.docxktm.extensions.substringBetween
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.models.TextStyle
import io.github.alexmaryin.docxktm.models.footerTextStyle
import io.github.alexmaryin.docxktm.values.ptToTwips
import org.docx4j.wml.CTSimpleField
import org.docx4j.wml.P
import java.math.BigInteger

class Paragraph(style: ParagraphStyle?) : ContentProvider, ParagraphContent {
    private val paragraph = io.github.alexmaryin.docxktm.docxFactory.createP()

    override fun <T : Any> add(element: T) {
        paragraph.content.add(element)
    }

    fun getP(): P = paragraph

    init {
        style?.let {
            val paragraphProperties = io.github.alexmaryin.docxktm.docxFactory.createPPr()
            paragraphProperties.pStyle = io.github.alexmaryin.docxktm.docxFactory.createPPrBasePStyle().apply { `val` = it.styleName }
            paragraphProperties.jc = io.github.alexmaryin.docxktm.docxFactory.createJc().apply { `val` = it.alignment }
            paragraphProperties.spacing = io.github.alexmaryin.docxktm.docxFactory.createPPrBaseSpacing().apply {
                it.spacing.after?.let { after = BigInteger.valueOf(it.ptToTwips()) }
                it.spacing.before?.let { before = BigInteger.valueOf(it.ptToTwips()) }
                it.spacing.between?.let { line = BigInteger.valueOf(it.ptToTwips()) }
            }
            paragraph.pPr = paragraphProperties
        }
    }

    fun pageNumber(template: String? = null, style: TextStyle = footerTextStyle) {

        template?.let { text(it.substringBefore("#p"), style, breakLine = false) }

        val page = io.github.alexmaryin.docxktm.docxFactory.createPFldSimple(CTSimpleField().apply { instr = "PAGE \\* MERGEFORMAT" })
        paragraph.content.add(page)

        template?.let {
            text(it.substringBetween("#p", "#t"), style, breakLine = false)
            if (it.contains("#t")) {
                val total = io.github.alexmaryin.docxktm.docxFactory.createPFldSimple(
                    CTSimpleField().apply { instr = "NUMPAGES \\* MERGEFORMAT" }
                )
                paragraph.content.add(total)
            }
            text(it.substringAfter("#t"), style, breakLine = false)
        }
    }
}



