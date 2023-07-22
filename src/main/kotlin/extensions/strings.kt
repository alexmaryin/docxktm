package io.github.alexmaryin.docxktm.extensions


fun String.substringBetween(firstDelimiter: String, secondDelimiter: String): String {
    val startIndex = indexOf(firstDelimiter).coerceAtLeast(0)
    var lastIndex = indexOf(secondDelimiter)
    if (lastIndex == -1) lastIndex = length
    return substring(startIndex + firstDelimiter.length, lastIndex)
}
