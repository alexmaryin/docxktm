package io.github.alexmaryin.docxktm.extensions

import jakarta.xml.bind.JAXBElement
import org.docx4j.wml.P
import org.docx4j.wml.Tbl
import org.docx4j.wml.Tc
import org.docx4j.wml.Tr

fun Tbl.getRows(): List<Tr> = content.filterIsInstance<Tr>()

fun Tr.getCells(): List<Tc> = content.filterIsInstance<JAXBElement<Tc>>().map { it.value }

fun Tc.getParagraphs(): List<P> = content.filterIsInstance<P>()

fun Tc.getNestedTables(): List<Tbl> = content.filterIsInstance<JAXBElement<Tbl>>().map { it.value }