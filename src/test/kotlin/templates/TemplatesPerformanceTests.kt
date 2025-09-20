package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.DocxNew
import io.github.alexmaryin.docxktm.DocxOpen
import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.parts.text
import io.github.alexmaryin.docxktm.values.Paths
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class TemplatesPerformanceTests : TemplatesTestBase() {

    /**
     * Next variable can be used between 1 and 5000 on my own PC (Macbook M4 16 Gb)
     * I don't know if it literally depends on PC config but over 5400 repetitions
     * test crashes with Stack overflow (sic!) during MVEL2 evaluations.
     * Limit addresses to Java.HashMap overflow.
     *
     * But if you put for instance 500 vars you may note that MVEL2 evaluation
     * faster in two times than **fastest** docx4j method by variables' replacement!
     *
     * Output for 5000 reps:
     * Template created in 466 ms
     * replacement map created in 14 ms
     * Merging by variables took 665 ms
     * Merging by MVEL2 took 457 ms
     *
     * Output for 500 reps:
     * Template created in 452 ms
     * replacement map created in 6 ms
     * Merging by variables took 215 ms
     * Merging by MVEL2 took 96 ms
     */
    private val repetitions = 5_000

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `Measure simple variable replacements performance`() {
        val mappings = mutableMapOf<String, String>()
        val time = measureTimeMillis {
            DocxNew(Paths.TEMPLATES_DIR + "performance_template.docx") {
                body {
                    repeat(repetitions) { idx ->
                        paragraph { text($$"New value mapping field No.$${idx + 1} ${var$$idx}") }
                    }
                }
            }
        }
        println("Template created in $time ms")
        val mapTime = measureTimeMillis {
            repeat(repetitions) { idx ->
                mappings += "var$idx" to Uuid.random().toString()
            }
        }
        println("replacement map created in $mapTime ms")
        val mergeTime = measureTimeMillis {
            DocxOpen(
                Paths.TEMPLATES_DIR + "performance_template.docx",
                Paths.TEST_DOCX_DIR + "simple_performance_map.docx"
            ) {
                body {
                    mergeTemplateStrMap(mappings)
                }
            }
        }
        println("Merging by variables took $mergeTime ms")
        val mergeMVELTime = measureTimeMillis {
            DocxTemplate(
                Paths.TEMPLATES_DIR + "performance_template.docx",
                Paths.TEST_DOCX_DIR + "mvel_performance_map.docx"
            ) {
                fromMap(mappings)
            }
        }
        println("Merging by MVEL2 took $mergeMVELTime ms")

    }
}