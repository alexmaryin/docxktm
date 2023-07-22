package io.github.alexmaryin.docxktm.values

import org.docx4j.wml.SectPr.PgSz

sealed class PageSize(open val width: Int, open val height: Int) {
    object Letter : PageSize(15840, 12240)
    object A5 : PageSize(11907, 8391)
    object A4 : PageSize(16839, 11907)
    object A3 : PageSize(23814, 16839)
    data class Custom(override val width: Int, override val height: Int) : PageSize(width, height)

    fun isEqual(size: PgSz) = size.w.toInt() == width && size.h.toInt() == height
}
