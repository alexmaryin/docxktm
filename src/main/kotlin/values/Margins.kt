package io.github.alexmaryin.docxktm.values

import io.github.alexmaryin.docxktm.models.TableWidth

/**
 * margins for a table content.
 * Has companion [all] which specify all margins with the same value
 */
data class Margins(
    val top: TableWidth = TableWidth.Auto,
    val bottom: TableWidth = TableWidth.Auto,
    val left: TableWidth = TableWidth.Auto,
    val right: TableWidth = TableWidth.Auto
) {
    companion object {
        fun all(size: Int) = Margins(
            TableWidth.Fixed(size),
            TableWidth.Fixed(size),
            TableWidth.Fixed(size),
            TableWidth.Fixed(size)
        )
    }
}
