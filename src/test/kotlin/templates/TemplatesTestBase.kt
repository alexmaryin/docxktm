package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxNew
import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.models.RowStyle
import io.github.alexmaryin.docxktm.models.TextStyle
import io.github.alexmaryin.docxktm.models.headerRowStyle
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.table
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.values.Alignment
import io.github.alexmaryin.docxktm.values.Paths
import java.io.File
import java.time.LocalDate
import kotlin.test.BeforeTest

internal open class TemplatesTestBase {
    val simpleDict = mapOf(
        "name" to "Alex",
        "age" to "39",
        "date" to "01.01.2023",
        "undefined" to "undefined"
    )

    data class User(
        val name: String,
        val age: Int,
        val gender: Gender = Gender.UNSPECIFIED
    )

    enum class Gender { MALE, FEMALE, UNSPECIFIED }

    val richDict = mapOf(
        "user" to User("Alex", 39),
        "date" to LocalDate.of(2023, 1, 1),
        "active" to true
    )

    val sampleJson = """
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

    @BeforeTest
    fun setup() {
        if (!File(Paths.TEMPLATES_DIR + "template_rich.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "template_rich.docx") {
                body {
                    paragraph { text("Second template test.") }
                    paragraph { text($$"Hello, my name is ${user.name} and I’m ${user.age}.") }
                    paragraph { text($$"Today is ${date}.") }
                    paragraph { text($$"User is ${active == true ? 'Active' : 'Inactive'}.") }
                    paragraph { text($$"Paragraph with error: ${undefined}") }
                }
            }
        }

        if (!File(Paths.TEMPLATES_DIR + "template_MVEL2.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "template_MVEL2.docx") {
                body {
                    paragraph {
                        text($$"Order No: ${orderId} ", TextStyle(bold = true))
                        text($$"ordered at ${orderedAt}", TextStyle(italic = true))
                    }
                    paragraph {
                        text($$"Customer: ${customer.name} (${customer.age} years old) ${customer.vip == true ? 'is VIP' : 'is regular'}")
                    }
                    paragraph {
                        text("Items:", TextStyle(italic = true, size = 14))
                        text("@foreach{item : items}")
                    }
                    paragraph {
                        text($$"- ${item.sku}: ${item.desc} x${item.qty} @ ${item.unitPrice} = ${item.qty * item.unitPrice}")
                        text("@end{}")
                    }
                    paragraph{}
                    paragraph { text($$"TOTAL: ${total}", TextStyle(bold = true, size = 18)) }
                }
            }
        }

        if (!File(Paths.TEMPLATES_DIR + "template_MVEL2_table.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "template_MVEL2_table.docx") {
                body {
                    paragraph {
                        text($$"Order No: ${orderId} ", TextStyle(bold = true))
                        text($$"ordered at ${orderedAt}", TextStyle(italic = true))
                    }
                    paragraph {
                        text($$"Customer: ${customer.name} (${customer.age} years old) ${customer.vip == true ? 'is VIP' : 'is regular'}")
                    }
                    paragraph { text("Items:", TextStyle(italic = true, size = 14)) }
                    table {
                        row(RowStyle(alignment = Alignment.CENTER, headerRow = true)) {
                            textInCell("SKU")
                            textInCell("Description")
                            textInCell("Quantity")
                            textInCell("Unit Price")
                            textInCell("Total")
                        }
                        row {
                            textInCell($$"@foreach{item : items} ${item.sku}")
                            textInCell($$"${item.desc}")
                            textInCell($$"${item.qty}")
                            textInCell($$"${item.unitPrice}")
                            textInCell($$"${item.qty * item.unitPrice}")
                        }
                    }
                    paragraph{}
                    paragraph { text($$"TOTAL: ${total}", TextStyle(bold = true, size = 18)) }
                }
            }
        }


        if (!File(Paths.TEMPLATES_DIR + "template.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "template.docx") {
                body {
                    paragraph {
                        text($$"This template is made by ${name} for testing ${library} library.")
                    }
                    paragraph {
                        text($$"Today is: ${today}.")
                    }
                }
            }
        }
        if (!File(Paths.TEMPLATES_DIR + "default_filler_template.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "default_filler_template.docx") {
                body {
                    paragraph {
                        text($$"Hello ${name}, welcome to ${place}.")
                    }
                    paragraph {
                        text($$"Your age is ${age} and status is ${status}.")
                    }
                    paragraph {
                        text($$"Missing field: ${missing}.")
                    }
                }
            }
        }
        if (!File(Paths.TEMPLATES_DIR + "klass_template.docx").exists()) {
            DocxNew(Paths.TEMPLATES_DIR + "klass_template.docx") {
                body {
                    paragraph {
                        text($$"Hello ${user.name}, welcome to MVEL2.")
                    }
                    paragraph {
                        text($$"Your age is ${user.age} and gender is ${user.gender.name() == 'MALE' ? 'male' : 'female'}.")
                    }
                    paragraph {
                        text($$"Simple field: ${simpleField}.")
                    }
                    paragraph {
                        text($$"Missing field: ${missing}.")
                    }
                }
            }
        }
    }
}
