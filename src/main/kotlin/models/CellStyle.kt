package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.CellTextDirection
import io.github.alexmaryin.docxktm.values.Margins
import org.docx4j.wml.STVerticalJc

data class CellStyle(
    val spannedCells: Int? = null,
    val noWrap: Boolean = false,
    val borderStyle: TableBorder? = null,
    val padding: Margins? = null,
    val width: TableWidth = TableWidth.Auto,
    val textDirection: CellTextDirection = CellTextDirection.TOP_LEFT,
    val verticalAlign: STVerticalJc = STVerticalJc.TOP
)
