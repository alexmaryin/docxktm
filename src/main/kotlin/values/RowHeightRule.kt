package io.github.alexmaryin.docxktm.values

import org.docx4j.wml.STHeightRule

enum class RowHeightRule(val value: STHeightRule) {
    AUTO(STHeightRule.AUTO),
    EXACT(STHeightRule.EXACT),
    AT_LEAST(STHeightRule.AT_LEAST),
}