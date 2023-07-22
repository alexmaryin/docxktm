package io.github.alexmaryin.docxktm.extensions


/**
 * Returns substring between two substrings
 * @param firstDelimiter If [firstDelimiter] is not in the string it starts from first symbol
 * @param secondDelimiter If [secondDelimiter] is not in the string it takes its full length
 */
fun String.substringBetween(firstDelimiter: String, secondDelimiter: String): String {
    val startIndex = indexOf(firstDelimiter).coerceAtLeast(0)
    var lastIndex = indexOf(secondDelimiter)
    if (lastIndex == -1) lastIndex = length
    return substring(startIndex + firstDelimiter.length, lastIndex)
}
