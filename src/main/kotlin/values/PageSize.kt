package io.github.alexmaryin.docxktm.values

import org.docx4j.wml.SectPr.PgSz

/**
 * defines page size with [width] and [height]
 * Has predefined values for A5, A4, A3, Letter formats or Custom
 */
sealed class PageSize(open val width: Int, open val height: Int) {
    data object Letter : PageSize(15840, 12240)
    data object A5 : PageSize(11907, 8391)
    data object A4 : PageSize(16839, 11907)
    data object A3 : PageSize(23814, 16839)
    data class Custom(override val width: Int, override val height: Int) : PageSize(width, height)

    fun isEqual(size: PgSz) = size.w.toInt() == width && size.h.toInt() == height
}
