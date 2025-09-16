package io.github.alexmaryin.docxktm.templates

import jakarta.xml.bind.JAXBElement
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import org.docx4j.wml.Text

internal fun getAllTextElements(obj: Any?): List<Text> {
    val result = mutableListOf<Text>()
    if (obj == null) return result

    when (obj) {
        is Text -> result.add(obj)
        is JAXBElement<*> -> {
            val v = obj.value
            if (v is Text) result.add(v)
            result.addAll(getAllTextElements(v))
        }

        else -> { // it's a container object
            val children = try {
                org.docx4j.TraversalUtil.getChildrenImpl(obj)
            } catch (_: Exception) {
                null
            }
            if (children != null) {
                for (child in children) {
                    result.addAll(getAllTextElements(child))
                }
            }
        }
    }
    return result
}

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

fun JsonElement.findJsonArray(name: String): JsonArray? = when (this) {
    is JsonObject -> this[name] as? JsonArray
        ?: values.asSequence().mapNotNull { it.findJsonArray(name) }.firstOrNull()
    is JsonArray -> asSequence().mapNotNull { it.findJsonArray(name) }.firstOrNull()
    else -> null
}


