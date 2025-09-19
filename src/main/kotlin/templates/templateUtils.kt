package io.github.alexmaryin.docxktm.templates

import kotlinx.serialization.json.*

internal fun JsonElement.toPlain(): Any? = when (this) {
    is JsonObject -> this.mapValues { it.value.toPlain() }
    is JsonArray -> this.map { it.toPlain() }
    is JsonPrimitive -> when {
        this.isString -> this.content
        this.booleanOrNull != null -> this.boolean
        this.doubleOrNull != null -> this.double
        else -> this.content
    }

    JsonNull -> null
}

internal fun JsonElement.toPlainMap(): Map<String, Any> = when (this) {
    is JsonObject -> this.mapValues { (_, v) -> v.toPlainValue() }
    else -> error("Top-level JSON element must be an object or null")
}

private fun JsonElement.toPlainValue(): Any = when (this) {
    is JsonObject -> this.mapValues { (_, v) -> v.toPlainValue() }
    is JsonArray -> this.map { it.toPlainValue() }
    is JsonPrimitive -> when {
        this.isString -> this.content
        this.booleanOrNull != null -> this.boolean
        this.doubleOrNull != null -> this.double
        else -> this.content
    }

    JsonNull -> ""
}

internal fun JsonElement.findJsonArray(name: String): JsonArray? = when (this) {
    is JsonObject -> this[name] as? JsonArray
        ?: values.asSequence().mapNotNull { it.findJsonArray(name) }.firstOrNull()

    is JsonArray -> asSequence().mapNotNull { it.findJsonArray(name) }.firstOrNull()
    else -> null
}

fun Map<String, Any?>.findCollection(key: String): Collection<Any?>? {
    this[key]?.let { v -> if (v is Collection<*>) return v }

    for ((_, value) in this) {
        when (value) {
            is Map<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                val found = (value as? Map<String, Any?>)?.findCollection(key)
                if (found != null) return found
            }

            is Collection<*> -> {
                for (elem in value) {
                    if (elem is Map<*, *>) {
                        @Suppress("UNCHECKED_CAST")
                        val found = (elem as? Map<String, Any?>)?.findCollection(key)
                        if (found != null) return found
                    }
                }
            }
        }
    }
    return null
}

/**
 * Follows a dot-separated path like "item.subitem.subsubitem.detailsList"
 * through nested maps and returns the value **only if the last segment is a List**.
 * Returns null when:
 *   - any key in the path is missing,
 *   - any intermediate value is not a map,
 *   - the final value is not a List.
 */
@Suppress("UNCHECKED_CAST")
fun Map<String, Any>.listAtPath(path: String): List<*>? {
    val segments = path.split('.')
    var current: Any? = this

    for (i in segments.indices) {
        val key = segments[i]
        current = (current as? Map<String, Any>)?.get(key) ?: return null
        if (i == segments.lastIndex && current is List<*>) return current
    }
    return null
}

