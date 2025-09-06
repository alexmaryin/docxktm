package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.Alignment
import io.github.alexmaryin.docxktm.values.UnderlineStyle
import io.github.alexmaryin.docxktm.values.WordColor

/**
 * defines text properties.
 *
 * There are some predefined values to simplicity:
 *
 * [normalTextStyle] - default style
 *
 * [headerRowStyle] - specifies only bold style for a header
 *
 * [footerTextStyle] - specifies only small font size for a footer
 */
data class TextStyle(
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underlined: UnderlineStyle? = null,
    val capitalized: Boolean = false,
    val small: Boolean = false,
    val strike: Boolean = false,
    val doubleStrike: Boolean = false,
    val color: WordColor = WordColor.Default,
    val size: Int? = null,
    val align: Alignment = Alignment.LEFT
)

val normalTextStyle = TextStyle()

val headerRowStyle = TextStyle(bold = true)

val footerTextStyle = TextStyle(size = 8)
