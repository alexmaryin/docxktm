package io.github.alexmaryin.docxktm.values

/**
 * defines paragraph spacing
 */
data class ParagraphSpacing(
    val after: Int? = null,
    val before: Int? = null,
    val between: Int? = null
)

val defaultSpacing = ParagraphSpacing()

/**
 * converts points to Twips whatever it means
 */
fun Int.ptToTwips() = this * 20L
