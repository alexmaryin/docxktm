package io.github.alexmaryin.docxktm.models

import io.github.alexmaryin.docxktm.values.Alignment
import io.github.alexmaryin.docxktm.values.ParagraphSpacing
import io.github.alexmaryin.docxktm.values.Styles
import io.github.alexmaryin.docxktm.values.defaultSpacing

/**
 * defines paragraph properties
 */
data class ParagraphStyle(
    val styleName: String = Styles.NORMAL,
    val alignment: Alignment = Alignment.LEFT,
    val spacing: ParagraphSpacing = defaultSpacing
)
