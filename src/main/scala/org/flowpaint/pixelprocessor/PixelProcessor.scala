package org.flowpaint.pixelprocessor

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.{Set, Map}
import util.{DataSample, Configuration}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

abstract class PixelProcessor extends Configuration {

  /**
   * Processes the variables.  The variableNameMappings are used to map names used by this PixelProcessor to the ones that are in the variable array.
   * It allows parameter passing (in and out) by name.  If no mapping is specified, the default parameter name is used directly as variable name.
   */
  def processPixel( variables : DataSample, variableNameMappings : Map[String, String], generalSettings : Data )

  /**
   * Returns the parameter names that are used by this PixelProcessor, if it is called with the specified variableNameMappings.
   * Used to ensure that all variable names are present when compiling PixelProgram:s.
   */
  def getUsedVariableNames( variableNameMappings : Map[String, String] ) : List[String] = { throw new UnsupportedOperationException("PixelProgram:s are not yet implemented") }

  /**
   * Takes a map from variable names to their position in the variable array.
   */
  def generateCode( variableIndexes : Map[String, Int] ) : String = { throw new UnsupportedOperationException("PixelProgram:s are not yet implemented") }


  def getSourceVariable( name : String, variableNameMappings : Map[String, String] ) : String = {

    // First check if there is some string setting with the same name, allowing configuration time mapping
    var s = getStringProperty( name, name )

    // Then map using parameter name mapping
    variableNameMappings.get(s) match {
      case Some(str) => str
      case None => s
    }
  }

  protected def hasMappedVar( name : String, variables : DataSample, variableNameMappings : Map[String, String] ) : Boolean = {
    (getSettings containsProperty name) ||
      (variables contains getSourceVariable(name, variableNameMappings))
  }

  protected def getMappedVar( name : String, default : Float, variables : DataSample, variableNameMappings : Map[String, String] ) : Float = {

    if ( getSettings.containsFloatProperty( name ) ) {
      // Check if there is a value for the parameter, if so, use it directly
      getFloatProperty( name, 0 )
    }
    else {
      // Get value from input variables
      var s = getSourceVariable(name, variableNameMappings)

      variables.getProperty( s, default )
    }
  }

  protected def setMappedVar( name : String, value : Float, variables : DataSample, variableNameMappings : Map[String, String] )  {

    var s = getSourceVariable(name, variableNameMappings)

    if (s != null && s != "") variables.setProperty( s, value )
  }

  protected def setMappedVarWithoutRedirect( name : String, value : Float, variables : DataSample, variableNameMappings : Map[String, String] )  {

    // Map variable if needed
    val s = variableNameMappings.get(name) match {
      case Some(str) => str
      case None => name
    }

    if (s != null && s != "") variables.setProperty( s, value )
  }

  protected def setMappedVars( source : DataSample, variables : DataSample, variableNameMappings : Map[String, String] ) {
    val sourceNames = source.getPropertyNames

    sourceNames foreach {name : String => {
      val value = source.getProperty( name, 0f )

      setMappedVar( name, value, variables, variableNameMappings )
    }}

  }


  protected def scaleOffset( value : Float, prefix : String, variables : DataSample, variableNameMappings : Map[String, String] ) : Float = {
    val scale = getMappedVar( prefix + "Scale", 1f, variables, variableNameMappings )
    val offset = getMappedVar( prefix + "Offset", 0f, variables, variableNameMappings )

    value * scale + offset
  }


  protected def getScaleOffsetVar(name : String, default : Float, variables : DataSample, variableNameMappings : Map[String, String] ) : Float = {
    val value = getMappedVar( name, default, variables, variableNameMappings )
    scaleOffset( value, name, variables, variableNameMappings )
  }

  protected def setScaleOffsetVar(name : String, value : Float, variables : DataSample, variableNameMappings : Map[String, String] ) {
    val scaleOffsetValue = scaleOffset( value, name, variables, variableNameMappings )
    setMappedVar( name, scaleOffsetValue, variables, variableNameMappings )
  }

}