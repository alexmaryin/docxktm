# DocxKtm

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maven Central](https://img.shields.io/maven-central/v/io.github.alexmaryin/docxktm)](https://search.maven.org/search?q=g:io.github.alexmaryin%20AND%20a:docxktm) ![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?logo=kotlin)

`DocxKtm` is a powerful and intuitive Kotlin DSL (Domain-Specific Language) for creating and manipulating Microsoft Word `.docx` files. Built as a wrapper around the robust `docx4j` Java library, `DocxKtm` simplifies document generation by providing a clean, readable, and expressive API, allowing you to define complex documents with idiomatic Kotlin code.

Whether you need to generate reports, invoices, or any other structured document, `DocxKtm` streamlines the process, freeing you from the verbosity of the underlying XML and Java APIs.

## Features

*   **Fluent Kotlin DSL:** A declarative, easy-to-read API for building documents.
*   **Document Creation & Editing:** Create new documents from scratch or open and modify existing ones.
*   **Rich Content:** Easily add and style paragraphs, text, tables, images, headers, and footers.
*   **Powerful Templating:**
    *   Simple key-value placeholder replacement (`${variable}`).
    *   Advanced type-safe templating for classes, numbers, dates, and currencies with custom formatting.
    *   **Rich MVEL2 syntax support** for complex expressions, conditional logic, and loops within templates.
    *   **Dynamic table population** from collections of objects or JSON strings.
*   **Comprehensive Styling:** Apply styles to text, paragraphs, tables, rows, and cells.
*   **Full `docx4j` Access:** Drop down to the underlying `docx4j` API for advanced or unsupported features.

## Installation

Add the library to your project's dependencies.

#### Gradle (Kotlin DSL)

```kotlin
// build.gradle.kts
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.alexmaryin:docxktm:1.3.0") // Replace with the latest version
}
```

#### Maven

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.github.alexmaryin</groupId>
    <artifactId>docxktm</artifactId>
    <version>1.3.0</version> <!-- Replace with the latest version -->
</dependency>
```

## Getting Started

### Creating a New Document

Use the `DocxNew` function to create a new document from scratch. The `body` block is the entry point for adding content.

```kotlin
import io.github.alexmaryin.docxktm.DocxNew
import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text

fun main() {
    DocxNew("output/hello_world.docx") {
        body {
            paragraph {
                text("Hello, DocxKtm!")
            }
        }
    }
}
```

### Opening and Modifying an Existing Document

Use `DocxOpen` to load an existing `.docx` file, modify it, and save the changes to a new file (or overwrite the original).

```kotlin
import io.github.alexmaryin.docxktm.DocxOpen
import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.values.Styles

fun main() {
    DocxOpen("input.docx", "modified.docx") {
        body {
            paragraph(ParagraphStyle(styleName = Styles.TITLE)) {
                text("This is a new title added to the document.")
            }
        }
    }
}
```

## DSL Reference

### Paragraphs and Text

The core of any document is its text. `DocxKtm` provides a simple way to add paragraphs and styled text runs.

```kotlin
import io.github.alexmaryin.docxktm.models.*
import io.github.alexmaryin.docxktm.values.*

// ... inside body { ... }
paragraph(ParagraphStyle(styleName = Styles.TITLE, alignment = Alignment.CENTER)) {
    text("Main Title")
}

paragraph(ParagraphStyle(spacing = ParagraphSpacing(before = 12, after = 24))) {
    text("This is the first run of text. ")
    text(
        "This is a second run, but bold and blue.",
        style = TextStyle(bold = true, color = WordColor.Blue)
    )
}

paragraph {
    text("This is the first line.", breakLine = true)
    text("This is the second line in the same paragraph.")
}
```

### Tables

Create complex tables with full control over styling for the table, rows, and cells.

```kotlin
import io.github.alexmaryin.docxktm.models.*
import io.github.alexmaryin.docxktm.values.*
import io.github.alexmaryin.docxktm.parts.table

// ... inside body { ... }
table(
    TableStyle(
        width = TableWidth.PageFit,
        borders = TableBorder.All(BorderStyle(type = TableBorderType.SINGLE, width = 1))
    )
) {
    // Header Row
    row(RowStyle(headerRow = true, height = RowHeight.AtLeast(20))) {
        cell(CellStyle(width = TableWidth.Percent(30))) {
            paragraph { text("Header 1", style = headerRowStyle) }
        }
        cell(CellStyle(width = TableWidth.Percent(70))) {
            paragraph { text("Header 2", style = headerRowStyle) }
        }
    }

    // Data Row
    row {
        cell { paragraph { text("Data 1.1") } }
        cell { paragraph { text("Data 1.2") } }
    }

    // Row with spanned cells
    row {
        cell(CellStyle(spannedCells = 2, verticalAlign = VerticalAlign.CENTER)) {
            paragraph(ParagraphStyle(alignment = Alignment.CENTER)) {
                text("This cell spans two columns.")
            }
        }
    }
}
```

### Images

Embed images from files and control their size and placement.

```kotlin
import io.github.alexmaryin.docxktm.models.fromCmToEMU
import io.github.alexmaryin.docxktm.parts.imageFromFile

// ... inside body { ... }
paragraph(ParagraphStyle(alignment = Alignment.CENTER)) {
    // Bind the image first to make it available to the document
    val image = bindImage(
        filename = "path/to/your/image.png",
        description = "My awesome image"
    )
    imageFromFile(image)
}

paragraph {
    // You can also specify a size (in EMU - English Metric Units)
    val sizedImage = bindImage(
        filename = "path/to/your/image.png",
        width = 5.fromCmToEMU(), // Set width to 5cm, height will be scaled
        height = 3.fromCmToEMU() // Or set both width and height
    )
    imageFromFile(sizedImage)
}
```

### Headers and Footers

Add headers and footers to your document, including dynamic page numbers.

```kotlin
import io.github.alexmaryin.docxktm.extensions.header
import io.github.alexmaryin.docxktm.extensions.footer
import io.github.alexmaryin.docxktm.models.ParagraphStyle
import io.github.alexmaryin.docxktm.values.Alignment

// ... inside DocxNew { ... }
header(ParagraphStyle(alignment = Alignment.CENTER)) {
    text("My Document Header")
}

body {
    // ... main document content
}

footer(ParagraphStyle(alignment = Alignment.RIGHT)) {
    text("Page ")
    // #p is replaced by the current page number
    // #t is replaced by the total number of pages
    pageNumber("page #p of #t")
}
```

## Advanced Templating

`DocxKtm` offers a sophisticated templating engine. Create a `.docx` file with placeholders and populate it dynamically.

### MVEL2 Expression Language

Take full control of your templates with the MVEL2 expression language. This allows for complex logic, transformations, and data manipulation directly within your `.docx` template.

**Template file `mvel_template.docx`:**
```
> Customer: ${customer.name.toUpperCase()}
> 
> @if{customer.orders.size() > 0}
>   Recent Orders:
>   @foreach{order : customer.orders}
>     - Order #${order.id} - Amount: ${new java.text.DecimalFormat('$#,##0.00').format(order.amount)}
>   @end{}
> @else{}
>   No recent orders.
> @end{}
```

**Kotlin code:**

```kotlin
import io.github.alexmaryin.docxktm.templates.DocxTemplate

data class Customer(val name: String, val orders: List<Order>)
data class Order(val id: Int, val amount: Double)

fun generateMvelReport() {
    val customer = Customer(
        name = "John Doe",
        orders = listOf(Order(1, 120.50), Order(2, 75.00))
    )

    DocxTemplate("mvel_template.docx", "mvel_report.docx") {
        "customer" to customer
    }
}
```

### Dynamic Table Population

Populate tables dynamically from a list of objects or a JSON string. `DocxKtm` automatically creates new rows for each item in the collection.

#### From a Collection of Objects

Define a table with a single data row and use the `@foreach {item: collection_name}` syntax in the first cell of that row.

**Template file `table_template.docx`:**

| Product Name                    | Quantity          | Price                 |
|---------------------------------|-------------------|-----------------------|
| `@foreach {product : products}` | `${product.name}` | `${product.quantity}` | `${product.price}` |

**Kotlin code:**

```kotlin
import io.github.alexmaryin.docxktm.templates.DocxTemplate

data class Product(val name: String, val quantity: Int, val price: Double)

fun generateProductTable() {
    val products = listOf(
        Product("Laptop", 1, 1200.00),
        Product("Mouse", 2, 25.50),
        Product("Keyboard", 1, 75.00)
    )

    DocxTemplate("table_template.docx", "product_table.docx") {
        "products" to products
    }
}
```

#### From a JSON String

You can also populate a table directly from a JSON string representing an array of objects.

**Kotlin code:**

```kotlin
import io.github.alexmaryin.docxktm.templates.DocxTemplate

fun generateProductTableFromJson() {
    val productsJson = '''
        [
            {"name": "Laptop", "quantity": 1, "price": 1200.00},
            {"name": "Mouse", "quantity": 2, "price": 25.50},
            {"name": "Keyboard", "quantity": 1, "price": 75.00}
        ]
    '''

    DocxTemplate("table_template.docx", "product_table_from_json.docx") {
        "products" to productsJson
    }
}
```

### Simple String-based Templating

For quick and simple replacements, you can use `mergeTemplate`.

```kotlin
import io.github.alexmaryin.docxktm.extensions.body

// ... inside DocxOpen { ... }
body {
    val replacements = hashMapOf(
        "customer_name" to "Jane Doe",
        "order_date" to "2023-10-27",
        "total_amount" to "$150.00"
    )
    mergeTemplate(replacements)
}
```

## Contributing

Contributions are welcome! Please feel free to submit a pull request or create an issue for bugs, questions, or feature requests.

1.  Fork the repository.
2.  Create a new feature branch (`git checkout -b feature/your-feature`).
3.  Commit your changes (`git commit -am 'Add some feature'`).
4.  Push to the branch (`git push origin feature/your-feature`).
5.  Create a new Pull Request.

## License

Copyright 2025 Alex Maryin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
