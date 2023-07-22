package io.github.alexmaryin.docxktm.extensions

import jakarta.xml.bind.JAXBElement
import org.docx4j.wml.P
import org.docx4j.wml.Tbl
import org.docx4j.wml.Tc
import org.docx4j.wml.Tr

/**
 * returns table rows as a list of [Tr]
 */
fun Tbl.getRows(): List<Tr> = content.filterIsInstance<Tr>()

/**
 * returns cells in table row as a list of [Tc]
 */
fun Tr.getCells(): List<Tc> = content.filterIsInstance<JAXBElement<Tc>>().map { it.value }

/**
 * returns a list of paragraphs in the table cell [Tc]
 */
fun Tc.getParagraphs(): List<P> = content.filterIsInstance<P>()

/**
 * returns a nested table [Tbl] from the origin table
 */
fun Tc.getNestedTables(): List<Tbl> = content.filterIsInstance<JAXBElement<Tbl>>().map { it.value }