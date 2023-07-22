package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.ptToTwips
import org.docx4j.wml.JcEnumeration
import org.docx4j.wml.STHeightRule

data class RowStyle(
    val alignment: JcEnumeration = JcEnumeration.LEFT,
    val padding: TableWidth = TableWidth.Auto,
    val headerRow: Boolean = false,
    val height: RowHeight = RowHeight.Auto,
)

sealed class RowHeight(
    val value: Long,
    val type: STHeightRule
) {
    object Auto : RowHeight(0, STHeightRule.AUTO)
    data class AtLeast(val height: Int) : RowHeight(height.ptToTwips(), STHeightRule.AT_LEAST)
    data class Exact(val height: Int) : RowHeight(height.ptToTwips(), STHeightRule.EXACT)
}
