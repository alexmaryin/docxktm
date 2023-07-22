package io.github.alexmaryin.docxktm.values

sealed class WordColor(val name: String) {
    object Auto : WordColor("auto")
    object Default : WordColor("000000")
    object Black : WordColor("000000")
    object Red : WordColor("FF0000")
    object Blue : WordColor("0000FF")
    data class Custom(val hexCode: String) : WordColor(hexCode)
}