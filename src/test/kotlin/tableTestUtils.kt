package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.getCells
import org.docx4j.wml.Tr

internal fun Tr.assertCellsEquals(vararg expected: String): Boolean {
    val actual = getCells()
    if (actual.size != expected.size) return false
    for (i in actual.indices) {
        if (actual[i].content.toString() != expected[i]) return false
    }
    return true
}

internal fun Tr.assertCellsEquals(expected: List<String>): Boolean =
    assertCellsEquals(*expected.toTypedArray())