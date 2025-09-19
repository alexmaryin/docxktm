package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxOpen
import io.github.alexmaryin.docxktm.extensions.body

/**
 * Open docx template file, process replacements map from the block and save result into output docx file
 * @param templateFilename filename of the template docx
 * @param outputFilename filename of the resulting docx
 * @param filler default value to replace placeholders when key is not found in replacements map
 * @param block lambda for replacement map populating as following:
 *
 *     "key" to "value"
 *     "key2" to "value2"
 *     "key3" to "value3", etc.
 *
 * After "value" you may use [with] infix fun to define two variants
 * of custom formatting:
 *
 *[io.github.alexmaryin.docxktm.values.NumberFormat] for Number types
 *
 *[io.github.alexmaryin.docxktm.values.DateFormat] for LocalDate or LocalDateTime types
 *
 *or enum [io.github.alexmaryin.docxktm.values.CurrencyFormat] for Number types
 * with RUB, USD, EUR or CHF variants
 *
 *      "key" to 1234.12345 with NumberFormat("0#,###")
 *      "key" to today() with DateFormat("dd MMMM yyyy")
 *      "key" to 2555.99 with CurrencyFormat.USD
 */
fun DocxTemplate(
    templateFilename: String,
    outputFilename: String,
    filler: String = "",
    block: DocxTemplateBuilder.() -> Unit) {
    DocxOpen(templateFilename, outputFilename, autoSave = true) {
        val builder = DocxTemplateBuilder()
        builder.block()
        builder.commitPending() // commit all pending replacements
        body {
            if (builder.replacements.isNotEmpty()) {
                mergeTemplateMap(builder.replacements, filler)
            }
            builder.jsonElement?.let {
                mergeTemplateJSON(it)
            }
        }
    }
}