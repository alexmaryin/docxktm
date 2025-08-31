package io.github.alexmaryin.docxktm.templates

import jakarta.xml.bind.JAXBElement
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

