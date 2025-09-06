package io.github.alexmaryin.docxktm.values

import org.docx4j.wml.STVerticalJc

enum class VerticalAlign(val value: STVerticalJc) {
    TOP(STVerticalJc.TOP),
    CENTER(STVerticalJc.CENTER),
    BOTTOM(STVerticalJc.BOTTOM),
    BOTH(STVerticalJc.BOTH)
}