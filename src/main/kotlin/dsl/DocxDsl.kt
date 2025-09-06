package io.github.alexmaryin.docxktm.dsl

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class DocxDsl

/*
How to use it:

@DocxDsl
class TableBuilder {
    fun row(block: RowBuilder.() -> Unit) { ... }
}

*/