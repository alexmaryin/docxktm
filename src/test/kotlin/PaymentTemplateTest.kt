package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.templates.DocxTemplate
import io.github.alexmaryin.docxktm.values.CurrencyFormat
import io.github.alexmaryin.docxktm.values.DateFormat
import io.github.alexmaryin.docxktm.values.Paths
import java.time.LocalDateTime
import kotlin.test.Test

class PaymentTemplateTest {

    @Test
    fun `Open and populate payment order`() {
        DocxTemplate(
            templateFilename = Paths.TEMPLATES_DIR + "paymentOrder.docx",
            outputFilename = Paths.TEST_DOCX_DIR + "payment1.docx"
        ) {
            "orderNo" to 123
            "date" to LocalDateTime.now() with DateFormat("dd.MM.yyyy")
            "amount" to 10000.0 with CurrencyFormat.RUB
            "amountWords" to "Десять тысяч рублей 00 копеек"
            "payer" to "ООО \"ПЕРДИЩЕВО\""
            "payerVAT" to "12345678912"
            "payerKPP" to "1234567890"
            "accountNo" to "44000000111100101023"
            "correspondNo" to "30181000100000000012"
            "payerBank" to "Сбербанк ПАО"
            "BIC" to "404525111"
            "recipientBank" to "Городской банк СП ОК РУ"
            "recipientBIC" to "11122345"
            "recipientAccountNo" to "2210000111100101023"
            "recipientCorrNo" to "30181000100000000012"
            "recipientVAT" to "12345678912"
            "recipientKPP" to "1234567890"
            "recipient" to "ООО \"РОГА и КОПЫТА\""
            "KBK" to ""
            "OKTMO" to ""
            "paymentName" to "Оплата по счету N 122923 от 30.03.2025, 10000 руб., НДС 20%"
        }
    }
}