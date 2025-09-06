package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.*
import io.github.alexmaryin.docxktm.models.*
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.table
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.values.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TableTest {
    @Test
    fun `Create simple 3x3 table in docx`() {
        DocxNew(Paths.TEST_DOCX_DIR + "simple_table.docx") {
            body {
                table(TableStyle(borders = TableBorder.All(BorderStyle(width = 1)))) {
                    row {
                        repeat(3) {
                            cell {
                                paragraph(ParagraphStyle(alignment = Alignment.CENTER)) {
                                    text("caption ${it + 1}", normalTextStyle.copy(bold = true))
                                }
                            }
                        }
                    }
                    repeat(3) { row ->
                        row {
                            repeat(3) { col ->
                                cell {
                                    paragraph { text("row ${row + 2} cell ${col + 1}") }
                                }
                            }
                        }
                    }
                }
            }
        }

        DocxOpen(Paths.TEST_DOCX_DIR + "simple_table.docx", autoSave = false) {
            body {
                val tables = getTables()
                assertTrue { tables.size == 1 }
                val rows = tables.first().getRows()
                assertTrue { rows.size == 4 }
                val firstRow = rows.first()
                val cells = firstRow.getCells()
                assertTrue { cells.size == 3 }
                cells.forEachIndexed { idx, cell ->
                    assertEquals(expected = "caption ${idx + 1}", actual = "${cell.getParagraphs().first()}")
                }
            }
        }
    }

    @Test
    fun `Create nested table inside cell`() {
        DocxNew(Paths.TEST_DOCX_DIR + "nested_table.docx") {
            body {
                paragraph {
                    text("Nested table example", normalTextStyle.copy(size = 28, align = Alignment.CENTER))
                }
                table(TableStyle(borders = TableBorder.OnlyMargin(BorderStyle(width = 1)))) {
                    row {
                        cell {
                            paragraph { text("Simple cell") }
                            paragraph { text("with second paragraph") }
                        }
                        cell {
                            paragraph { text("Nested table:") }
                            table(
                                TableStyle(
                                    layoutType = TableLayout.FIXED,
                                    width = TableWidth.Percent(75),
                                    borders = TableBorder.All(
                                        BorderStyle(
                                            type = TableBorderType.DOT_DASH,
                                            width = 2,
                                            color = WordColor.Blue
                                        )
                                    )
                                )
                            ) {
                                row {
                                    cell {
                                        paragraph { text("First nested cell") }
                                    }
                                    cell {
                                        paragraph { text("Second nested cell") }
                                    }
                                }
                            }
                        }
                    }
                    row {
                        cell {
                            paragraph { text("Continue first table") }
                        }
                        cell {
                            paragraph { text("Continue second column") }
                        }
                    }
                }
            }
        }

        DocxOpen(Paths.TEST_DOCX_DIR + "nested_table.docx", autoSave = false) {
            body {
                val tables = getTables()
                assertTrue { tables.size == 1 }
                val nestedTable = tables.first().getRows().first().getCells().last().getNestedTables().firstOrNull()
                assertNotNull(nestedTable)
                val rows = nestedTable.getRows()
                assertTrue { rows.size == 1 }
            }
        }
    }

    @Test
    fun `Test row styles`() {
        DocxNew(Paths.TEST_DOCX_DIR + "table_rows.docx") {
            body {
                table {
                    row(
                        RowStyle(
                            headerRow = true,
                            height = RowHeight.AtLeast(50)
                        )
                    ) {
                        repeat(3) {
                            cell(CellStyle(width = TableWidth.Fixed(500 - 100 * it))) {
                                paragraph(ParagraphStyle(alignment = Alignment.CENTER)) {
                                    text("caption ${it + 1}")
                                }
                            }
                        }
                    }
                    row {
                        repeat(3) { cell { paragraph { text("normal text ${it + 1}") } } }
                    }
                }
            }
        }
    }

    @Test
    fun `Test cell styles`() {
        DocxNew(Paths.TEST_DOCX_DIR + "table_cells.docx") {
            body {
                table {
                    row {
                        repeat(3) { cell { paragraph { text("cell ${it + 1}") } } }
                    }
                    row {
                        cell(
                            CellStyle(spannedCells = 2, verticalAlign = VerticalAlign.CENTER)
                        ) { paragraph { text("Spanned 2 cells") } }
                        cell { paragraph { text("Last cell") } }
                    }
                    row {
                        cell { paragraph { text("First cell") } }
                        cell(
                            CellStyle(spannedCells = 2, verticalAlign = VerticalAlign.BOTTOM)
                        ) { paragraph { text("Spanned 2 cells") } }
                    }
                }
            }
        }
    }
}