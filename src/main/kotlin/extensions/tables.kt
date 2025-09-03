package io.github.alexmaryin.docxktm.extensions

import jakarta.xml.bind.JAXBElement
import org.docx4j.wml.*

/**
 * A generic helper to extract docx elements from a content list.
 * It handles both direct elements (like [Tr] in [Tbl]) and elements wrapped
 * in [JAXBElement] (like [Tc] in [Tr]).
 */
internal inline fun <reified T : Any> List<Any>.getDocxElements(): List<T> =
    mapNotNull {
        when {
            it is T -> it
            it is JAXBElement<*> && it.value is T -> it.value as T
            else -> null
        }
    }

/**
 * returns table rows as a list of [Tr]
 */
fun Tbl.getRows(): List<Tr> = content.getDocxElements()

/**
 * returns cells in table row as a list of [Tc]
 */
fun Tr.getCells(): List<Tc> = content.getDocxElements()

/**
 * returns a list of paragraphs in the table cell [Tc]
 */
fun Tc.getParagraphs(): List<P> = content.getDocxElements()

/**
 * returns a nested table [Tbl] from the origin table
 */
fun Tc.getNestedTables(): List<Tbl> = content.getDocxElements()