package io.github.alexmaryin.docxktm.parts.tables

import io.github.alexmaryin.docxktm.docxFactory
import io.github.alexmaryin.docxktm.models.CellStyle
import io.github.alexmaryin.docxktm.models.toCTBorder
import io.github.alexmaryin.docxktm.models.toTblWidth
import io.github.alexmaryin.docxktm.parts.ContentProvider
import org.docx4j.wml.Tc
import java.math.BigInteger

internal class Cell(style: CellStyle?) : ContentProvider {

    private val cell = docxFactory.createTc()

    override fun <T : Any> add(element: T) {
        cell.content.add(element)
    }

    init {
        style?.let { cellStyle ->
            val cellProperties = docxFactory.createTcPr()
            cellStyle.spannedCells?.let {
                cellProperties.gridSpan = docxFactory.createTcPrInnerGridSpan().apply {
                    `val` = BigInteger.valueOf(it.toLong())
                }
            }
            if (cellStyle.noWrap) cellProperties.noWrap = docxFactory.createBooleanDefaultTrue()
            cellStyle.borderStyle?.let {
                cellProperties.tcBorders = docxFactory.createTcPrInnerTcBorders().apply {
                    top = it.top?.toCTBorder()
                    bottom = it.bottom?.toCTBorder()
                    left = it.left?.toCTBorder()
                    right = it.right?.toCTBorder()
                }
            }
            cellStyle.padding?.let {
                cellProperties.tcMar = docxFactory.createTcMar().apply {
                    top = it.top.toTblWidth()
                    bottom = it.bottom.toTblWidth()
                    left = it.left.toTblWidth()
                    right = it.right.toTblWidth()
                }
            }
            cellProperties.tcW
            cellProperties.tcW = cellStyle.width.toTblWidth()
            cellProperties.textDirection = docxFactory.createTextDirection().apply { `val` = cellStyle.textDirection.xml }
            cellProperties.vAlign = docxFactory.createCTVerticalJc().apply { `val` = cellStyle.verticalAlign.value }
            cell.tcPr = cellProperties
        }
    }

    fun getTc(): Tc = cell
}