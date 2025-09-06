package io.github.alexmaryin.docxktm.values

import org.docx4j.wml.STTblLayoutType

enum class TableLayout(val value: STTblLayoutType) {
    FIXED(STTblLayoutType.FIXED),
    AUTOFIT(STTblLayoutType.AUTOFIT)
}