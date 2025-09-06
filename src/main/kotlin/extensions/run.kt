package io.github.alexmaryin.docxktm.extensions

import io.github.alexmaryin.docxktm.docxFactory
import io.github.alexmaryin.docxktm.models.TextStyle
import org.docx4j.wml.BooleanDefaultTrue
import org.docx4j.wml.Color
import org.docx4j.wml.R
import org.docx4j.wml.RPr
import java.math.BigInteger

/**
 * @return font size for Run-block
 */
fun R.fontSize() = rPr.sz.`val`.toInt() / 2

/**
 * applies style of [TextStyle] for Run-block
 */
fun RPr.applyProperties(style: TextStyle) = apply {
    if (style.bold) b = BooleanDefaultTrue()
    if (style.italic) i = BooleanDefaultTrue()
    if (style.capitalized) caps = BooleanDefaultTrue()
    if (style.small) smallCaps = BooleanDefaultTrue()
    if (style.doubleStrike) dstrike = BooleanDefaultTrue()
    if (style.strike) strike = BooleanDefaultTrue()
    style.underlined?.let { u = docxFactory.createU().apply { `val` = it.value } }
    style.size?.let { sz = docxFactory.createHpsMeasure().apply { `val` = BigInteger.valueOf(it * 2L) } }
    color = Color().apply { `val` = style.color.name }
}
