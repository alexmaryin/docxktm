package io.github.alexmaryin.docxktm.models

data class WordImage(
    val filename: String,
    val description: String = "",
    val id: Long,
    val width: Long? = null,
    val height: Long? = null,
    val bytes: ByteArray
)

fun Number.fromCmToEMU() = (this.toFloat() * 360000).toLong()

fun Number.fromCmToTWIP() = (this.toFloat() * 567).toLong()