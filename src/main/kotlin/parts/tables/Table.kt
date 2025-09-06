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
        style?.let { borderStyle ->
            val tableProperties = docxFactory.createTblPr()
            tableProperties.tblBorders = docxFactory.createTblBorders().apply {
                borderStyle.borders.bottom?.let { bottom = it.toCTBorder() }
                borderStyle.borders.top?.let { top = it.toCTBorder() }
                borderStyle.borders.left?.let { left = it.toCTBorder() }
                borderStyle.borders.right?.let { right = it.toCTBorder() }
                borderStyle.borders.horizontalEdges?.let { insideH = it.toCTBorder() }
                borderStyle.borders.verticalEdges?.let { insideV = it.toCTBorder() }
            }
            tableProperties.tblLayout = docxFactory.createCTTblLayoutType().apply {
                type = borderStyle.layoutType.value
            }
            tableProperties.tblW = docxFactory.createTblWidth().apply {
                type = borderStyle.width.type
                w = BigInteger.valueOf(borderStyle.width.value)
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