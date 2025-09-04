package io.github.alexmaryin.docxktm.parts.tables

import io.github.alexmaryin.docxktm.docxFactory
import io.github.alexmaryin.docxktm.models.RowStyle
import io.github.alexmaryin.docxktm.models.TableStyle
import io.github.alexmaryin.docxktm.models.toCTBorder
import org.docx4j.wml.Tbl
import java.math.BigInteger

class Table(style: TableStyle?) {
    private val table: Tbl = docxFactory.createTbl()

    init {
        style?.let {
            val tableProperties = docxFactory.createTblPr()
            tableProperties.tblBorders = docxFactory.createTblBorders().apply {
                it.borders.bottom?.let { bottom = it.toCTBorder() }
                it.borders.top?.let { top = it.toCTBorder() }
                it.borders.left?.let { left = it.toCTBorder() }
                it.borders.right?.let { right = it.toCTBorder() }
                it.borders.horizontalEdges?.let { insideH = it.toCTBorder() }
                it.borders.verticalEdges?.let { insideV = it.toCTBorder() }
            }
            tableProperties.tblLayout = docxFactory.createCTTblLayoutType().apply {
                type = it.layoutType
            }
            tableProperties.tblW = docxFactory.createTblWidth().apply {
                type = it.width.type
                w = BigInteger.valueOf(it.width.value)
            }
            table.tblPr = tableProperties
        }
    }

    fun row(
        style: RowStyle = RowStyle(),
        block: Row.() -> Unit
    ) {
        val row = Row(style)
        block(row)
        table.content.add(row.getTr())
    }

    fun getTbl() = table
}