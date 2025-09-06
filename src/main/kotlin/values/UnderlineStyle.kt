package io.github.alexmaryin.docxktm.values

import org.docx4j.wml.UnderlineEnumeration

enum class UnderlineStyle(val value: UnderlineEnumeration) {
    SINGLE(UnderlineEnumeration.SINGLE),
    WORDS(UnderlineEnumeration.WORDS),
    DOUBLE(UnderlineEnumeration.DOUBLE),
    THICK(UnderlineEnumeration.THICK),
    DOTTED(UnderlineEnumeration.DOTTED),
    DOTTED_HEAVY(UnderlineEnumeration.DOTTED_HEAVY),
    DASH(UnderlineEnumeration.DASH),
    DASHED_HEAVY(UnderlineEnumeration.DASHED_HEAVY),
    DASH_LONG(UnderlineEnumeration.DASH_LONG),
    DASH_LONG_HEAVY(UnderlineEnumeration.DASH_LONG_HEAVY),
    DOT_DASH(UnderlineEnumeration.DOT_DASH),
    DASH_DOT_HEAVY(UnderlineEnumeration.DASH_DOT_HEAVY),
    DOT_DOT_DASH(UnderlineEnumeration.DOT_DOT_DASH),
    DASH_DOT_DOT_HEAVY(UnderlineEnumeration.DASH_DOT_DOT_HEAVY),
    WAVE(UnderlineEnumeration.WAVE),
    WAVY_HEAVY(UnderlineEnumeration.WAVY_HEAVY),
    WAVY_DOUBLE(UnderlineEnumeration.WAVY_DOUBLE),
    NONE(UnderlineEnumeration.NONE),
}