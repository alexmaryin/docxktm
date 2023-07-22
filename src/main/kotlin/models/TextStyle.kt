package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.WordColor
import org.docx4j.wml.JcEnumeration
import org.docx4j.wml.UnderlineEnumeration

data class TextStyle(
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underlined: UnderlineEnumeration? = null,
    val capitalized: Boolean = false,
    val small: Boolean = false,
    val strike: Boolean = false,
    val doubleStrike: Boolean = false,
    val color: WordColor = WordColor.Default,
    val size: Int? = null,
    val align: JcEnumeration = JcEnumeration.LEFT
)

val normalTextStyle = TextStyle()

val headerRowStyle = TextStyle(bold = true)

val footerTextStyle = TextStyle(size = 8)
