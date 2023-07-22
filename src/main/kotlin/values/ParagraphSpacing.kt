package io.github.alexmaryin.docxktm.values

data class ParagraphSpacing(
    val after: Int? = null,
    val before: Int? = null,
    val between: Int? = null
)

val defaultSpacing = ParagraphSpacing()

fun Int.ptToTwips() = this * 20L
