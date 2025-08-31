package io.github.alexmaryin.docxktm.values

import java.util.Locale

enum class CurrencyFormat(val symbol: String, val locale: Locale) {
    RUB("₽", Locale("ru", "RU")),
    USD("$", Locale.US),
    EUR("€", Locale.GERMANY),
    CHF("CHF", Locale("de", "CH"))
}
