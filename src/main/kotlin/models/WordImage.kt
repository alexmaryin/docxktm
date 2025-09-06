package io.github.alexmaryin.docxktm.models

/**
 * source image which should be saved inside the docx container
 * [width] and [height] should be defined in EMU (English metric units) units
 * or converted by:
 *
 * [fromPxToEMU] - from pixels to EMU
 *
 * [fromCmToEMU] - from centimeters to EMU (1/360000 of 1 cm)
 *
 * [fromCmToTWIP] - from centimeters to twentieth of a point (1/1440 of 1 inch)
 *
 * There are 635 EMUs per twip, 6,350 EMUs per half-point,
 * 12,700 EMUs per point, and 914,400 EMUs per inch.
 */
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

fun Number.fromPxToEMU() = (this.toFloat() * 9525L).toLong()