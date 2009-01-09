package org.flowpaint.pixelprocessor

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.{Set, Map}
import java.lang.Object
import util.{DataSample, Configuration}

/**
 *
 *
 * @author Hans Haggstrom
 */

abstract class PixelProcessor(initializerTemplate: String, loopedTemplate: String) extends Configuration {

    /**
     * Processes the variables.  The variableNameMappings are used to map names used by this PixelProcessor to the ones that are in the variable array.
     * It allows parameter passing (in and out) by name.  If no mapping is specified, the default parameter name is used directly as variable name.
     */
    def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings: Data)

    /**
     * Returns the parameter names that are used by this PixelProcessor, if it is called with the specified variableNameMappings.
     * Used to ensure that all variable names are present when compiling PixelProgram:s.
     */
    def getUsedVariableNames(variableNameMappings: Map[String, String]): List[String] = {

        def getNames(entry: String, prefix: String, postfix: String): List[String] = {

            if (entry startsWith prefix) {
                val (name, default) = calculateVariableAndDefault(entry.substring(prefix.length), null)

                val name1 = name + postfix

                if (name1 != null) {
                    val redirName = getSourceVariable(name1)
                    List(redirName)
                }
                else Nil
            }
            else Nil
        }

        var entries = tokenize(initializerTemplate)

        var foundNames: List[String] = Nil

        while (!entries.isEmpty) {
            entries = entries.tail

            if (!entries.isEmpty) {
                val entry: String = entries.head

                foundNames = foundNames ::: getNames(entry, "getString", "")
                foundNames = foundNames ::: getNames(entry, "setString", "")

                foundNames = foundNames ::: getNames(entry, "getScaleOffsetFloat", "")
                foundNames = foundNames ::: getNames(entry, "getScaleOffsetFloat", "Scale")
                foundNames = foundNames ::: getNames(entry, "getScaleOffsetFloat", "Offset")

                foundNames = foundNames ::: getNames(entry, "setScaleOffsetFloat", "")
                foundNames = foundNames ::: getNames(entry, "setScaleOffsetFloat", "Scale")
                foundNames = foundNames ::: getNames(entry, "setScaleOffsetFloat", "Offset")

                entries = entries.tail
            }
        }

        foundNames
    }

    /**
     * Takes a map from variable names to their position in the variable array.
     */
    def generateCode(variableNames: List[String],
                    nameToIndex: Map[String, Int],
                    generalSettings: Data,
                    variableArrayName: String,
                    nextUniqueId: () => Int): (String, String) = {

        (parseTemplate(nameToIndex, initializerTemplate, generalSettings, variableArrayName, nextUniqueId),
                parseTemplate(nameToIndex, loopedTemplate, generalSettings, variableArrayName, nextUniqueId))
    }

    private def tokenize(s: String): List[String] = if (!s.startsWith("$")) s.split("\\$").toList
    else (" " + s).split("\\$").toList

    private def calculateVariableAndDefault(entry: String, defaultDefault: String): (String, String) = {
        val commaIndex = entry.indexOf(",")
        val variableName = (if (commaIndex < 0) entry else entry.substring(0, commaIndex)).trim
        val defaultValue = if (commaIndex < 0 || commaIndex + 1 >= entry.size) defaultDefault else entry.substring(commaIndex + 1)
        (variableName, defaultValue)
    }


    /**
     * Replace accesses to variables with correct array indexing operations or strings
     */
    private def parseTemplate(nameToIndex: Map[String, Int],
                             template: String,
                             generalSettings: Data,
                             variableArrayName: String,
                             nextUniqueId: () => Int): String = {

        def parseFloat(variableName: String, defaultValue: String, code: StringBuilder) {
            val redirectedVariableName = getSourceVariable(variableName)

            if (getSettings.containsFloatProperty(variableName)) {
                code append (getSettings.getFloatProperty(variableName, 0).toString + "f")
            }
            else if (nameToIndex.contains(redirectedVariableName)) {
                code append variableArrayName + "[" + nameToIndex(redirectedVariableName) + "]"
            }
            else {
                code append defaultValue
            }
        }

        def parseGetScaleOffsetFloat(variableName: String, defaultValue: String, code: StringBuilder) {

            code append " ( "
            parseFloat(variableName, defaultValue, code)
            code append " * "
            parseFloat(variableName + "Scale", "1f", code)
            code append " + "
            parseFloat(variableName + "Offset", "0f", code)
            code append " ) "
        }

        def parseSetScaleOffsetFloat(variableName: String, defaultValue: String, code: StringBuilder) {

            parseFloat(variableName, defaultValue, code)
            code append " = "
            parseFloat(variableName + "Offset", "0f", code)
            code append " + "
            parseFloat(variableName + "Scale", "1f", code)
            code append " * "
        }

        def parseUniqueName(variableName: String, defaultValue: String, code: StringBuilder) {

            // Generates an unique string for this processor.  Useful for making sure variable names dont clash
            code append ("pixelProcessor" + nextUniqueId())
        }

        def parseString(variableName: String, defaultValue: String, code: StringBuilder) {
            val stringValue = getMappedString(variableName, null, generalSettings)

            if (stringValue == null) code append defaultValue
            else code append ("\"" + stringValue + "\"")
        }


        def parseVariable(entry: String, prefix: String, parser: (String, String, StringBuilder) => Unit, defaultValue: String, code: StringBuilder) {
            if (entry startsWith prefix) {
                val (name, default) = calculateVariableAndDefault(entry.substring(prefix.length), defaultValue)

                parser(name, default, code)
            }

        }

        var entries: List[String] = tokenize(template)

        val code = new StringBuilder()

        while (!entries.isEmpty) {
            code append entries.head
            entries = entries.tail

            if (!entries.isEmpty) {
                val entry: String = entries.head

                parseVariable(entry, "getString ", parseString, "null", code)
                parseVariable(entry, "getFloat ", parseFloat, "0f", code)
                parseVariable(entry, "getScaleOffsetFloat ", parseGetScaleOffsetFloat, "0f", code)
                parseVariable(entry, "setScaleOffsetFloat ", parseSetScaleOffsetFloat, "throwAwayValue", code)
                parseVariable(entry, "setFloat ", parseFloat, "throwAwayValue", code)
                parseVariable(entry, "uniqueName", parseUniqueName, "", code)

                entries = entries.tail
            }
        }

        code.toString
    }

    def getSourceVariable(name: String): String = getStringProperty(name, name)

    def getSourceVariable(name: String, variableNameMappings: Map[String, String]): String = {
        // First check if there is some string setting with the same name, allowing configuration time mapping
        var s = getSourceVariable(name)

        // Then map using parameter name mapping
        variableNameMappings.get(s) match {
            case Some(str) => str
            case None => s
        }
    }

    protected def hasMappedVar(name: String, variables: DataSample, variableNameMappings: Map[String, String]): Boolean = {
        (getSettings containsProperty name) ||
                (variables contains getSourceVariable(name, variableNameMappings))
    }

    protected def getMappedVar(name: String, default: Float, variables: DataSample, variableNameMappings: Map[String, String]): Float = {

        if (getSettings.containsFloatProperty(name)) {
            // Check if there is a value for the parameter, if so, use it directly
            getFloatProperty(name, 0)
        }
        else {
            // Get value from input variables
            var s = getSourceVariable(name, variableNameMappings)

            variables.getProperty(s, default)
        }
    }

    protected def getMappedString(name: String, default: String, generalSettings: Data): String = {
        val proeprtyName = getStringProperty(name + "Property", name)
        val defaultValue = getStringProperty(name, default)

        generalSettings.getStringProperty(proeprtyName, defaultValue)
    }

    protected def setMappedVar(name: String, value: Float, variables: DataSample, variableNameMappings: Map[String, String]) {

        var s = getSourceVariable(name, variableNameMappings)

        if (s != null && s != "") variables.setProperty(s, value)
    }

    protected def setMappedVarWithoutRedirect(name: String, value: Float, variables: DataSample, variableNameMappings: Map[String, String]) {

        // Map variable if needed
        val s = variableNameMappings.get(name) match {
            case Some(str) => str
            case None => name
        }

        if (s != null && s != "") variables.setProperty(s, value)
    }

    protected def setMappedVars(source: DataSample, variables: DataSample, variableNameMappings: Map[String, String]) {
        val sourceNames = source.getPropertyNames

        sourceNames foreach {
            name: String => {
                val value = source.getProperty(name, 0f)

                setMappedVar(name, value, variables, variableNameMappings)
            }
        }

    }


    protected def scaleOffset(value: Float, prefix: String, variables: DataSample, variableNameMappings: Map[String, String]): Float = {
        val scale = getMappedVar(prefix + "Scale", 1f, variables, variableNameMappings)
        val offset = getMappedVar(prefix + "Offset", 0f, variables, variableNameMappings)

        value * scale + offset
    }


    protected def getScaleOffsetVar(name: String, default: Float, variables: DataSample, variableNameMappings: Map[String, String]): Float = {
        val value = getMappedVar(name, default, variables, variableNameMappings)
        scaleOffset(value, name, variables, variableNameMappings)
    }

    protected def setScaleOffsetVar(name: String, value: Float, variables: DataSample, variableNameMappings: Map[String, String]) {
        val scaleOffsetValue = scaleOffset(value, name, variables, variableNameMappings)
        setMappedVar(name, scaleOffsetValue, variables, variableNameMappings)
    }

}