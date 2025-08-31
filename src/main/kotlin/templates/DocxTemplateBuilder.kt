package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.values.CurrencyFormat
import io.github.alexmaryin.docxktm.values.DateFormat
import io.github.alexmaryin.docxktm.values.NumberFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DocxTemplateBuilder {
    internal val replacements = mutableMapOf<String, String>()
    private val pending = mutableListOf<PendingReplacement<*>>()

    infix fun String.to(value: String) { replacements[this] = value }

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
        replacements[this.key] = formatter.format(this.value)
        pending.remove(this)
    }

    infix fun PendingNumber.with(currency: CurrencyFormat) {
        val symbols = DecimalFormatSymbols(currency.locale).apply {
            groupingSeparator = if (currency == CurrencyFormat.USD) ',' else ' '
        }
        val formatter = DecimalFormat("#,##0.00", symbols)
        val formatted = formatter.format(this.value)
        replacements[this.key] = if (currency == CurrencyFormat.USD) "${currency.symbol}$formatted"
            else "$formatted ${currency.symbol}"
        pending.remove(this)
    }

    infix fun PendingDate.with(format: DateFormat) {
        val formatter = DateTimeFormatter.ofPattern(format.pattern)
        val formatted = when (val v = this.value) {
            is LocalDate -> v.format(formatter)
            is LocalDateTime -> v.format(formatter)
            else -> v.toString()
        }
        replacements[this.key] = formatted
        pending.remove(this)
    }

    internal fun commitPending() {
        pending.forEach { it.commitDefault() }
        pending.clear()
    }
}