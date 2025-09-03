package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.dsl.DocxDsl
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@DocxDsl
sealed class PendingReplacement<T>(val key: String, val value: T) {
    abstract fun commitDefault()
}

@DocxDsl
class PendingNumber(key: String, value: Number, private val builder: DocxTemplateBuilder) :
    PendingReplacement<Number>(key, value) {
    override fun commitDefault() {
        val symbols = DecimalFormatSymbols(Locale("ru", "RU")).apply {
            groupingSeparator = ' '
        }
        val formatter = DecimalFormat("#,##0.###", symbols)
        builder.replacements[key] = formatter.format(value)
    }
}

@DocxDsl
class PendingDate(key: String, value: Any, private val builder: DocxTemplateBuilder) :
    PendingReplacement<Any>(key, value) {
    override fun commitDefault() {
        val formatted = when (value) {
            is LocalDate -> value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            is LocalDateTime -> value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            else -> value.toString()
        }
        builder.replacements[key] = formatted
    }
}