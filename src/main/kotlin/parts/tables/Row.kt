package io.github.alexmaryin.docxktm.parts.tables

import io.github.alexmaryin.docxktm.docxFactory
import io.github.alexmaryin.docxktm.models.CellStyle
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.models.RowStyle
import io.github.alexmaryin.docxktm.models.TextStyle
import io.github.alexmaryin.docxktm.models.normalTextStyle
import io.github.alexmaryin.docxktm.parts.ContentProvider
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import org.docx4j.wml.Tr
import java.math.BigInteger

class Row(style: RowStyle?) : ContentProvider {
    private val row = docxFactory.createTr()

    override fun <T : Any> add(element: T) {
        row.content.add(element)
    }

    init {
        style?.let {
            val rowProperties = docxFactory.createTrPr()
            with(rowProperties.cnfStyleOrDivIdOrGridBefore) {
                add(
                    docxFactory.createCTTrPrBaseTrHeight(docxFactory.createCTHeight().apply {
                        hRule = it.height.type.value
                        `val` = BigInteger.valueOf(it.height.value)
                    })
                )
                add(
                    docxFactory.createCTTrPrBaseJc(docxFactory.createJc().apply { `val` = it.alignment.value })
                )
                add(
                    docxFactory.createCTTrPrBaseTblCellSpacing(docxFactory.createTblWidth().apply {
                        type = it.padding.type
                        w = BigInteger.valueOf(it.padding.value)
                    })
                )
                if (it.headerRow) add(
                    docxFactory.createCTTrPrBaseTblHeader(docxFactory.createBooleanDefaultTrue())
                )
            }
            row.trPr = rowProperties
        }
    }

    fun cell(
        style: CellStyle? = null,
        block: ContentProvider.() -> Unit
    ) {
        Cell(style).apply {
            block(this)
            this@Row.add(getTc())
        }
    }

    fun listInCells(
        values: List<Any>,
        textStyle: TextStyle? = null,
        paragraphStyle: ParagraphStyle? = null
    ) {
        values.forEach { value ->
            cell {
                paragraph(paragraphStyle) {
                    text(value.toString(), textStyle ?: normalTextStyle)
                }
            }
        }
    }

    fun textInCell(
        text: String,
        textStyle: TextStyle? = null
    ) {
       cell {
           paragraph {
               text(text, textStyle ?: normalTextStyle)
           }
       }
    }

    fun getTr(): Tr = row
}