package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.templates.mergeTemplateJSON
import io.github.alexmaryin.docxktm.templates.mergeTemplateMap
import io.github.alexmaryin.docxktm.templates.mergeTemplateStrMap
import io.github.alexmaryin.docxktm.values.Paths
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MVELTemplatesTest {

    private val simpleDict = mapOf(
        "name" to "Alex",
        "age" to "39",
        "date" to "01.01.2023",
        "undefined" to "undefined"
    )

    data class User(val name: String, val age: Int)

    private val richDict = mapOf(
        "user" to User("Alex", 39),
        "date" to LocalDate.of(2023, 1, 1),
        "active" to true
    )

    private val sampleJson = """
        {
          "orderId": "ORD-2025-09-15-001",
          "total": 299.99,
          "orderedAt": "2025-09-15T14:30:00Z",
          "customer": {
            "name": "Alice Smith",
            "age": 30,
            "vip": true
          },
          "items": [
            {
              "sku": "KB-2025-B",
              "desc": "Mechanical keyboard",
              "qty": 1,
              "unitPrice": 119.99
            },
            {
              "sku": "MS-2025-X",
              "desc": "Wireless mouse",
              "qty": 2,
              "unitPrice": 15.00
            },
            {
              "sku": "WB-2021-11",
              "desc": "Monitor FullHD",
              "qty": 1,
              "unitPrice": 150.00
            }
          ]
        }
    """.trimIndent()

    @Test
    fun `Simple docx template should return doc with merged fields`() {
        DocxOpen(
            Paths.TEMPLATES_DIR + "basic_template.docx",
            Paths.TEST_DOCX_DIR + "merged_result.docx"
        ) {
            body { mergeTemplateStrMap(simpleDict) }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "merged_result.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 3 }
                assertEquals(expected = "First template test.", actual = "${getParagraphs()[0]}")
                assertEquals(expected = "Hello, my name is Alex and I’m 39.", actual = "${getParagraphs()[1]}")
                assertEquals(expected = "Today is 01.01.2023.", actual = "${getParagraphs()[2]}")
            }
        }
    }

    @Test
    fun `Rich template should return doc with merged fields`() {
        DocxOpen(
            Paths.TEMPLATES_DIR + "template2.docx",
            Paths.TEST_DOCX_DIR + "merged_result2.docx"
        ) {
            body { mergeTemplateMap(richDict) }
        }
        DocxOpen(Paths.TEST_DOCX_DIR + "merged_result2.docx", autoSave = false) {
            body {
                assertTrue { getParagraphs().size == 5 }
                assertEquals(expected = "Second template test.", actual = "${getParagraphs()[0]}")
                assertEquals(expected = "Hello, my name is Alex and I’m 39.", actual = "${getParagraphs()[1]}")
                assertEquals(expected = "Today is 2023-01-01.", actual = "${getParagraphs()[2]}")
                assertEquals(expected = "User is Active.", actual = "${getParagraphs()[3]}")
                assertEquals(expected = "Paragraph with error: ", actual = "${getParagraphs()[4]}")
            }
        }
    }

    @Test
    fun `MVEL2 template with expressions and merged fields`() {
        DocxOpen(
            Paths.TEMPLATES_DIR + "template_MVEL2.docx",
            Paths.TEST_DOCX_DIR + "merged_result_MVEL2.docx"
        ) {
            body { mergeTemplateJSON(sampleJson) }
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
        DocxOpen(
            Paths.TEMPLATES_DIR + "template_MVEL2_table.docx",
            Paths.TEST_DOCX_DIR + "merged_result_MVEL2_table.docx"
        ) {
            body { mergeTemplateJSON(sampleJson) }
        }
    }
}