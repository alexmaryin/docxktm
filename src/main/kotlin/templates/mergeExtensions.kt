package io.github.alexmaryin.docxktm.templates

import io.github.alexmaryin.docxktm.extensions.getRows
import io.github.alexmaryin.docxktm.parts.Body
import kotlinx.serialization.json.JsonElement
import org.docx4j.XmlUtils
import org.docx4j.model.datastorage.migration.VariablePrepare
import org.docx4j.wml.Document
import org.docx4j.wml.Tr
import org.mvel2.integration.VariableResolver
import org.mvel2.integration.impl.MapVariableResolverFactory
import org.mvel2.integration.impl.SimpleValueResolver
import org.mvel2.templates.TemplateCompiler
import org.mvel2.templates.TemplateRuntime

/**
 * Populates docx template with fields from string dictionary [Map]
 *
 * Field for population should be bounded with ```${field_name}``` symbols
 *
 * The fastest way to merge template with dictionary in opinion of docx4j
 *
 * BUT...
 *
 * MVEL2 evaluations I have implemented here are faster in two times
 * especially on regular size of replacement maps. And even of virtual maps of 5000 variables.
 *
 * See for details test called `Measure simple variable replacements performance`
 */
fun Body.mergeTemplateStrMap(dict: Map<String, String>) {
    VariablePrepare.prepare(document)
    document.mainDocumentPart.variableReplace(dict)
}

private val mvelForeach = Regex("""@foreach\s*\{\s*([^}]+?)\s*}""")
private val mvelEnd = Regex("""@end\s*\{\s*}""")

/**
 * Populates docx template with JSON deserialized element [JsonElement]
 *
 * Parser understands JSON primitives like strings, numbers and boolean values,
 * json arrays which will be merged as list, nested json objects which
 * will be merged as plain Classes.
 *
 * Field for population should be bounded with ```${field_name}``` or ```${class.field_name}``` symbols
 *
 * For arrays should be used MVEL2 constriction for loop: @foreach { item : items } ..... @end{}
 * For details see documentation
 */
internal fun Body.mergeTemplateJSON(jsonElement: JsonElement) =
    mergeTemplateMap(jsonElement.toPlainMap())


internal fun resolverFactory(dict: Map<String, Any?>, filler: String) = object : MapVariableResolverFactory(dict) {
    override fun isResolveable(name: String?) = true
    override fun getVariableResolver(name: String?): VariableResolver? {
        if (!dict.containsKey(name)) return SimpleValueResolver(filler)
        return super.getVariableResolver(name)
    }
}

/**
 * Populates docx template with fields from dictionary of any types including
 * data classes of Kotlin, any Java classes. Enums should use suffix ```.name()``` inside template to work.
 *
 * Field for population should be bounded with ```${field_name}``` or ```${class.field_name}``` symbols
 *
 * Templates may contain MVEL2 statements
 */
internal fun Body.mergeTemplateMap(dict: Map<String, Any>, filler: String = "") {
    VariablePrepare.prepare(document)
    val jc = document.mainDocumentPart.jaxbContext
    //process tables first
    for (table in getTables()) {
        for (row in table.getRows()) {
            val rowXml = XmlUtils.marshaltoString(row, true, false, jc)
            // process row if it contains @foreach { item : items } MVEL2 instruction
            mvelForeach.find(rowXml)?.let { matchResult ->
                val loopDefinition = matchResult.groupValues[1] // extract { item : items } names
                val (itemVar, collectionVar) = loopDefinition.split(":").map { it.trim() }
                dict.listAtPath(collectionVar)?.let { array ->
                    val templateRowXml = rowXml.replace(mvelForeach, "").replace(mvelEnd, "")
                    val newRows = buildList {
                        for (item in array) {
                            val itemContext = mapOf(itemVar to item)
                            val newRowXml = TemplateRuntime.execute(
                                TemplateCompiler.compileTemplate(templateRowXml),
                                itemContext,
                                resolverFactory(itemContext, filler)
                            ).toString()
                            val newRow = XmlUtils.unmarshalString(newRowXml, jc, Tr::class.java) as Tr
                            add(newRow)
                        }
                    }
                    table.content.remove(row)
                    table.content.addAll(newRows)
                }
            }
        }
    }
    // process other merged fields outside any table
    val templateString = XmlUtils.marshaltoString(document.mainDocumentPart.jaxbElement, true, false, jc)
    val compiledTemplate = TemplateCompiler.compileTemplate(templateString)
    val factory = resolverFactory(dict, filler)
    val mergedString = TemplateRuntime.execute(compiledTemplate, dict, factory).toString()
    document.mainDocumentPart.jaxbElement = XmlUtils.unwrap(XmlUtils.unmarshalString(mergedString, jc)) as Document
}