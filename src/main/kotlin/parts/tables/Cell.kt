package io.github.alexmaryin.docxktm.parts.tables

import io.github.alexmaryin.docxktm.models.CellStyle
import io.github.alexmaryin.docxktm.models.toCTBorder
import io.github.alexmaryin.docxktm.models.toTblWidth
import io.github.alexmaryin.docxktm.parts.ContentProvider
import org.docx4j.wml.Tc
import java.math.BigInteger

class Cell(style: CellStyle?) : ContentProvider {

    private val cell = io.github.alexmaryin.docxktm.docxFactory.createTc()

    override fun <T : Any> add(element: T) {
        cell.content.add(element)
    }

    init {
        style?.let {
            val cellProperties = io.github.alexmaryin.docxktm.docxFactory.createTcPr()
            it.spannedCells?.let {
                cellProperties.gridSpan = io.github.alexmaryin.docxktm.docxFactory.createTcPrInnerGridSpan().apply {
                    `val` = BigInteger.valueOf(it.toLong())
                }
            }
            if (it.noWrap) cellProperties.noWrap = io.github.alexmaryin.docxktm.docxFactory.createBooleanDefaultTrue()
            it.borderStyle?.let {
                cellProperties.tcBorders = io.github.alexmaryin.docxktm.docxFactory.createTcPrInnerTcBorders().apply {
                    top = it.top?.toCTBorder()
                    bottom = it.bottom?.toCTBorder()
                    left = it.left?.toCTBorder()
                    right = it.right?.toCTBorder()
                }
            }
            it.padding?.let {
                cellProperties.tcMar = io.github.alexmaryin.docxktm.docxFactory.createTcMar().apply {
                    top = it.top.toTblWidth()
                    bottom = it.bottom.toTblWidth()
                    left = it.left.toTblWidth()
                    right = it.right.toTblWidth()
                }
            }
            cellProperties.tcW
            cellProperties.tcW = it.width.toTblWidth()
            cellProperties.textDirection = io.github.alexmaryin.docxktm.docxFactory.createTextDirection().apply { `val` = it.textDirection.xml }
            cellProperties.vAlign = io.github.alexmaryin.docxktm.docxFactory.createCTVerticalJc().apply { `val` = it.verticalAlign }
            cell.tcPr = cellProperties
        }
    }

    fun getTc(): Tc = cell
}