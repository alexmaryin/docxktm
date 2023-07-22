package io.github.alexmaryin.docxktm.parts.tables

import io.github.alexmaryin.docxktm.models.CellStyle
import io.github.alexmaryin.docxktm.models.RowStyle
import io.github.alexmaryin.docxktm.models.TextStyle
import io.github.alexmaryin.docxktm.models.normalTextStyle
import io.github.alexmaryin.docxktm.parts.ContentProvider
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import org.docx4j.wml.Tr
import java.math.BigInteger

class Row(style: RowStyle?) : ContentProvider {
    private val row = io.github.alexmaryin.docxktm.docxFactory.createTr()

    override fun <T : Any> add(element: T) {
        row.content.add(element)
    }

    init {
        style?.let {
            val rowProperties = io.github.alexmaryin.docxktm.docxFactory.createTrPr()
            with(rowProperties.cnfStyleOrDivIdOrGridBefore) {
                add(
                    io.github.alexmaryin.docxktm.docxFactory.createCTTrPrBaseTrHeight(io.github.alexmaryin.docxktm.docxFactory.createCTHeight().apply {
                        hRule = it.height.type
                        `val` = BigInteger.valueOf(it.height.value)
                    })
                )
                add(
                    io.github.alexmaryin.docxktm.docxFactory.createCTTrPrBaseJc(io.github.alexmaryin.docxktm.docxFactory.createJc().apply { `val` = it.alignment })
                )
                add(
                    io.github.alexmaryin.docxktm.docxFactory.createCTTrPrBaseTblCellSpacing(io.github.alexmaryin.docxktm.docxFactory.createTblWidth().apply {
                        type = it.padding.type
                        w = BigInteger.valueOf(it.padding.value)
                    })
                )
                if (it.headerRow) add(
                    io.github.alexmaryin.docxktm.docxFactory.createCTTrPrBaseTblHeader(io.github.alexmaryin.docxktm.docxFactory.createBooleanDefaultTrue())
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