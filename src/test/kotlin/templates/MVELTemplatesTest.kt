package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxOpen
import io.github.alexmaryin.docxktm.assertCellsEquals
import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.extensions.getRows
import io.github.alexmaryin.docxktm.values.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MVELTemplatesTest : TemplatesTestBase() {

    @Test
    fun `MVEL2 template with expressions and merged fields`() {
        DocxTemplate(
            Paths.TEMPLATES_DIR + "template_MVEL2.docx",
            Paths.TEST_DOCX_DIR + "merged_result_MVEL2.docx"
        ) {
            fromJsonString(sampleJson)
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "merged_result_MVEL2.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 8 }
                assertEquals(
                    expected = "Order No: ORD-2025-09-15-001 ordered at 2025-09-15T14:30:00Z",
                    actual = "${getParagraphs()[0]}"
                )
                assertEquals(
                    expected = "Customer: Alice Smith (30.0 years old) is VIP",
                    actual = "${getParagraphs()[1]}"
                )
                assertEquals(expected = "Items:", actual = "${getParagraphs()[2]}")
                assertEquals(
                    expected = "- KB-2025-B: Mechanical keyboard x1.0 @ 119.99 = 119.99",
                    actual = "${getParagraphs()[3]}"
                )
                assertEquals(
                    expected = "- MS-2025-X: Wireless mouse x2.0 @ 15.0 = 30.0",
                    actual = "${getParagraphs()[4]}"
                )
                assertEquals(
                    expected = "- WB-2021-11: Monitor FullHD x1.0 @ 150.0 = 150.0",
                    actual = "${getParagraphs()[5]}"
                )
                assertEquals(expected = "TOTAL: 299.99", actual = "${getParagraphs()[7]}")
            }
        }
    }

    @Test
    fun `MVEL2 template with expressions and merged fields with tables`() {
        DocxTemplate(
            Paths.TEMPLATES_DIR + "template_MVEL2_table.docx",
            Paths.TEST_DOCX_DIR + "merged_result_MVEL2_table.docx"
        ) {
            fromJsonString(sampleJson)
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "merged_result_MVEL2_table.docx", autoSave = false) {
            body {
                assertEquals(
                    expected = "Order No: ORD-2025-09-15-001 ordered at 2025-09-15T14:30:00Z",
                    actual = "${getParagraphs()[0]}"
                )
                assertEquals(
                    expected = "Customer: Alice Smith (30.0 years old) is VIP",
                    actual = "${getParagraphs()[1]}"
                )
                assertEquals(expected = "Items:", actual = "${getParagraphs()[2]}")
                assertTrue { getTables().size == 1 }
                with(getTables().first()) {
                    assertTrue { getRows().size == 4 }
                    getRows()[0].assertCellsEquals("SKU", "Quantity", "Unit Price", "Amount")
                    getRows()[1].assertCellsEquals("KB-2025-B", "1", "119.99", "119.99")
                    getRows()[2].assertCellsEquals("MS-2025-X", "2", "15.0", "30.0")
                    getRows()[3].assertCellsEquals("WB-2021-11", "1", "150.0", "150.0")
                }
            }
        }
    }

    @Test
    fun `MVEL2 template with complicated statements`() {

        data class Order(val id: Int, val amount: Double)
        data class Customer(val name: String, val orders: List<Order>)

        DocxTemplate(
            Paths.TEMPLATES_DIR + "mvel2_eval_template.docx",
            Paths.TEST_DOCX_DIR + "mvel2_eval_output.docx"
        ) {
            val customer = Customer(
                name = "John Doe",
                orders = listOf(Order(1, 120.50), Order(2, 75.00))
            )
            "customer" to customer
        }

        DocxOpen(Paths.TEST_DOCX_DIR + "mvel2_eval_output.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 2 }
                assertEquals(expected = "Customer: JOHN DOE", actual = "${getParagraphs()[0]}")
                assertEquals(
                    expected = "- Order #1 - Amount: 120.5- Order #2 - Amount: 75.0",
                    actual = "${getParagraphs()[1]}"
                )
            }
        }
    }
}