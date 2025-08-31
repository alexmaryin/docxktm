package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxOpen
import org.docx4j.openpackaging.packages.WordprocessingMLPackage

/**
 * Find placeholders in document and replace them with mapped values.
 * Each placeholder should be surrounded by curly braces: ${placeholder_name}.
 * @param replacements map of placeholder names to their values (String to String)
 */
fun WordprocessingMLPackage.processTemplate(replacements: Map<String, String>) {
    val placeholderRegex = Regex("""\$\{([^}]+)\}""")

    if (replacements.isEmpty()) return
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
            replacements[key]?.let { replacement ->
                newValue = newValue.replace("\${$key}", replacement)
            }
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
 * @param block lambda for replacement map populating as following:
 *
 *     "key" to "value"
 *     "key2" to "value2"
 *     "key3" to "value3", etc.
 */
fun DocxTemplate(templateFilename: String, outputFilename: String, block: DocxTemplateBuilder.() -> Unit) {
    DocxOpen(templateFilename, outputFilename, autoSave = true) {
        val builder = DocxTemplateBuilder()
        builder.block()
        builder.commitPending() // commit all pending replacements
        processTemplate(builder.replacements)
    }
}