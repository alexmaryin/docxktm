package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.WordColor
import io.github.alexmaryin.docxktm.values.ptToTwips
import org.docx4j.wml.CTBorder
import org.docx4j.wml.STBorder
import org.docx4j.wml.STTblLayoutType
import org.docx4j.wml.TblWidth
import java.math.BigInteger

data class TableStyle(
    val layoutType: STTblLayoutType = STTblLayoutType.AUTOFIT,
    val width: TableWidth = TableWidth.Auto,
    val borders: TableBorder = TableBorder.None
)

sealed class TableWidth(
    val value: Long,
    val type: String
) {
    object PageFit : TableWidth(100 * 50L, "pct")
    object Auto : TableWidth(0L, "auto")
    data class Fixed(val pt: Int) : TableWidth(pt.ptToTwips(), "dxa")
    data class Percent(val pct: Int) : TableWidth(pct * 50L, "pct")
}

fun TableWidth.toTblWidth() : TblWidth = io.github.alexmaryin.docxktm.docxFactory.createTblWidth().apply {
    w = BigInteger.valueOf(value)
    type = this@toTblWidth.type
}

sealed class TableBorder(
    open val bottom: BorderStyle? = null,
    open val top: BorderStyle? = null,
    open val left: BorderStyle? = null,
    open val right: BorderStyle? = null,
    open val horizontalEdges: BorderStyle? = null,
    open val verticalEdges: BorderStyle? = null
) {
    object None : TableBorder()
    data class OnlyMargin(val border: BorderStyle) : TableBorder(
        bottom = border, top = border, left = border, right = border
    )
    data class All(val border: BorderStyle) : TableBorder(
        bottom = border, top = border, left = border, right = border, horizontalEdges = border, verticalEdges = border
    )
    data class Custom(
        override val bottom: BorderStyle? = null,
        override val top: BorderStyle? = null,
        override val left: BorderStyle? = null,
        override val right: BorderStyle? = null,
        override val horizontalEdges: BorderStyle? = null,
        override val verticalEdges: BorderStyle? = null
    ) : TableBorder(bottom, top, left, right, horizontalEdges, verticalEdges)
}

data class BorderStyle(
    val type: STBorder = STBorder.SINGLE,
    val width: Int = 1,
    val color: WordColor = WordColor.Auto
)

fun BorderStyle.toCTBorder(): CTBorder = io.github.alexmaryin.docxktm.docxFactory.createCTBorder().apply {
    color = this@toCTBorder.color.name
    sz = BigInteger.valueOf(width * 8L)
    `val` = type
}

fun singleBorder(color: WordColor = WordColor.Auto): TableBorder = TableBorder.All(BorderStyle(color = color))
