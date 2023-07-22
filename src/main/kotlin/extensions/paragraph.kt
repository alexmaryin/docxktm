package io.github.alexmaryin.docxktm.extensions

import io.github.alexmaryin.docxktm.values.ParagraphSpacing
import org.docx4j.wml.JcEnumeration
import org.docx4j.wml.P
import org.docx4j.wml.R

/**
 * @return a list of all Run-blocks in paragraph
 */
fun P.runs() = content.filterIsInstance<R>()

/**
 * @return alignment of the paragraph
 */
fun P.alignment(): JcEnumeration = pPr?.jc?.`val` ?: JcEnumeration.LEFT

/**
 * @return [ParagraphSpacing] paragraph spacing
 */
fun P.spacing() = with(pPr.spacing) {
    ParagraphSpacing(
        after = after?.toInt()?.div(20),
        before = before?.toInt()?.div(20),
        between = line?.toInt()?.div(20)
    )
}