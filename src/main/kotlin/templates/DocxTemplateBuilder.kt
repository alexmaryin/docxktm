package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.dsl.DocxDsl
import io.github.alexmaryin.docxktm.values.CurrencyFormat
import io.github.alexmaryin.docxktm.values.DateFormat
import io.github.alexmaryin.docxktm.values.NumberFormat
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@DocxDsl
class DocxTemplateBuilder {
    internal val replacements = mutableMapOf<String, Any>()
    internal var jsonElement: JsonElement? = null

    private val pending = mutableListOf<PendingReplacement<*>>()

    infix fun <T : Any> String.to(value: T) { replacements[this] = value }

    infix fun String.to(value: Number): PendingNumber {
        val pendingNumber = PendingNumber(this, value, this@DocxTemplateBuilder)
        pending += pendingNumber
        return pendingNumber
    }

    infix fun String.to(value: LocalDate): PendingDate {
        val pendingDate = PendingDate(this, value, this@DocxTemplateBuilder)
        pending += pendingDate
        return pendingDate
    }

    infix fun String.to(value: LocalDateTime): PendingDate {
        val pendingDate = PendingDate(this, value, this@DocxTemplateBuilder)
        pending += pendingDate
        return pendingDate
    }

    infix fun PendingNumber.with(format: NumberFormat) {
        val symbols = DecimalFormatSymbols(Locale("ru", "RU")).apply {
            groupingSeparator = ' '
        }
        val formatter = DecimalFormat(format.pattern, symbols)
        this@DocxTemplateBuilder.replacements[this.key] = formatter.format(this.value)
        this@DocxTemplateBuilder.pending.remove(this)
    }

    infix fun PendingNumber.with(currency: CurrencyFormat) {
        val symbols = DecimalFormatSymbols(currency.locale).apply {
            groupingSeparator = if (currency == CurrencyFormat.USD) ',' else ' '
        }
        val formatter = DecimalFormat("#,##0.00", symbols)
        val formatted = formatter.format(this.value)
        this@DocxTemplateBuilder.replacements[this.key] = if (currency == CurrencyFormat.USD) "${currency.symbol}$formatted"
            else "$formatted ${currency.symbol}"
        this@DocxTemplateBuilder.pending.remove(this)
    }

    infix fun PendingDate.with(format: DateFormat) {
        val formatter = DateTimeFormatter.ofPattern(format.pattern)
        val formatted = when (val v = this.value) {
            is LocalDate -> v.format(formatter)
            is LocalDateTime -> v.format(formatter)
            else -> v.toString()
        }
        this@DocxTemplateBuilder.replacements[this.key] = formatted
        this@DocxTemplateBuilder.pending.remove(this)
    }

    fun fromJson(json: JsonElement) {
        jsonElement = json
    }

    fun fromJsonString(json: String) {
        jsonElement = try {
            Json.parseToJsonElement(json)
        } catch (_: SerializationException) {
            JsonNull
        }
    }

    fun fromMap(map: Map<String, Any>) {
        replacements.putAll(map)
    }

    internal fun commitPending() {
        pending.forEach { it.commitDefault() }
        pending.clear()
    }
}