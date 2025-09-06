package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxOpen
import org.docx4j.openpackaging.packages.WordprocessingMLPackage

/**
 * Find placeholders in document and replace them with mapped values.
 * Each placeholder should be surrounded by curly braces: ${placeholder_name}.
 * @param replacements map of placeholder names to their values (String to String)
 * @param defaultFiller default value to replace placeholders when key is not found in replacements map
 */
fun WordprocessingMLPackage.processTemplate(replacements: Map<String, String>, defaultFiller: String = "") {
    val placeholderRegex = Regex("""\$\{([^}]+)\}""")

    val textElements = getAllTextElements(mainDocumentPart)
    val placeholders = textElements.filter { element ->
        val value = element.value
        !value.isNullOrBlank() && placeholderRegex.containsMatchIn(value)
    }
    if (placeholders.isEmpty()) return

    for (placeholder in placeholders) {
        val original = placeholder.value ?: continue
        var newValue = original

        placeholderRegex.findAll(original).forEach { match ->
            val key = match.groupValues[1]
            val replacement = replacements[key] ?: defaultFiller
                newValue = newValue.replace("\${$key}", replacement)
            }
        if (newValue != original) {
            placeholder.value = newValue
        }
    }
}

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
        processTemplate(builder.replacements, filler)
    }
}