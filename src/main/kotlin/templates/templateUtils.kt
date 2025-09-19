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


fun JsonElement.findJsonArray(name: String): JsonArray? = when (this) {
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

