package io.github.alexmaryin.docxktm.values

import org.docx4j.wml.JcEnumeration

enum class Alignment(val value: JcEnumeration) {
    LEFT(JcEnumeration.LEFT),
    CENTER(JcEnumeration.CENTER),
    RIGHT(JcEnumeration.RIGHT),
    JUSTIFIED(JcEnumeration.BOTH),
    START(JcEnumeration.START),
    END(JcEnumeration.END),
    DISTRIBUTE(JcEnumeration.DISTRIBUTE),
    NUM_TAB(JcEnumeration.NUM_TAB),
    THAI_DISTRIBUTE(JcEnumeration.THAI_DISTRIBUTE),
    HIGH_KASHIDA(JcEnumeration.HIGH_KASHIDA),
    MEDIUM_KASHIDA(JcEnumeration.MEDIUM_KASHIDA),
    LOW_KASHIDA(JcEnumeration.LOW_KASHIDA),
}