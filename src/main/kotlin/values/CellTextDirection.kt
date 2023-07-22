package io.github.alexmaryin.docxktm.values

/**
 * text direction inside the cell
 */
enum class CellTextDirection(val xml: String) {
    TOP_LEFT("lrTb"),
    TOP_RIGHT("tbRl"),
    BOTTOM_LEFT("btLr")
}