package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.CellTextDirection
import io.github.alexmaryin.docxktm.values.Margins
import io.github.alexmaryin.docxktm.values.VerticalAlign

/**
 * defines cell properties
 */
data class CellStyle(
    val spannedCells: Int? = null,
    val noWrap: Boolean = false,
    val borderStyle: TableBorder? = null,
    val padding: Margins? = null,
    val width: TableWidth = TableWidth.Auto,
    val textDirection: CellTextDirection = CellTextDirection.TOP_LEFT,
    val verticalAlign: VerticalAlign = VerticalAlign.TOP
)
