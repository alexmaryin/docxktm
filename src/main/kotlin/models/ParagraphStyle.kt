package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.ParagraphSpacing
import io.github.alexmaryin.docxktm.values.Styles
import io.github.alexmaryin.docxktm.values.defaultSpacing
import org.docx4j.wml.JcEnumeration

/**
 * defines paragraph properties
 */
data class ParagraphStyle(
    val styleName: String = Styles.NORMAL,
    val alignment: JcEnumeration = JcEnumeration.LEFT,
    val spacing: ParagraphSpacing = defaultSpacing
)
