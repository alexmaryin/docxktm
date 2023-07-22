package io.github.alexmaryin.docxktm.values

/**
 * defines values for main colors or Custom variant with HEX code
 */
sealed class WordColor(val name: String) {
    data object Auto : WordColor("auto")
    data object Default : WordColor("000000")
    data object Black : WordColor("000000")
    data object Red : WordColor("FF0000")
    data object Blue : WordColor("0000FF")
    data class Custom(val hexCode: String) : WordColor(hexCode)
}