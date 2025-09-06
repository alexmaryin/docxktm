package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.Alignment
import io.github.alexmaryin.docxktm.values.RowHeightRule
import io.github.alexmaryin.docxktm.values.ptToTwips

/**
 * defines row table properties
 */
data class RowStyle(
    val alignment: Alignment = Alignment.LEFT,
    val padding: TableWidth = TableWidth.Auto,
    val headerRow: Boolean = false,
    val height: RowHeight = RowHeight.Auto,
)

sealed class RowHeight(
    val value: Long,
    val type: RowHeightRule
) {
    data object Auto : RowHeight(0, RowHeightRule.AUTO)
    data class AtLeast(val height: Int) : RowHeight(height.ptToTwips(), RowHeightRule.AT_LEAST)
    data class Exact(val height: Int) : RowHeight(height.ptToTwips(), RowHeightRule.EXACT)
}
