package io.github.alexmaryin.docxktm.extensions

import io.github.alexmaryin.docxktm.values.ParagraphSpacing
import org.docx4j.wml.JcEnumeration
import org.docx4j.wml.P
import org.docx4j.wml.R

fun P.runs() = content.filterIsInstance<R>()

fun P.alignment(): JcEnumeration = pPr?.jc?.`val` ?: JcEnumeration.LEFT

fun P.spacing() = with(pPr.spacing) {
    ParagraphSpacing(
        after = after?.toInt()?.div(20),
        before = before?.toInt()?.div(20),
        between = line?.toInt()?.div(20)
    )
}