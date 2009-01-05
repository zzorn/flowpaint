package org.flowpaint.pixelprocessors

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
  def processPixel( variables : DataSample, variableNameMappings : Map[String, String] )

  /**
   * Returns the parameter names that are used by this PixelProcessor, if it is called with the specified variableNameMappings.
   * Used to ensure that all variable names are present when compiling PixelProgram:s.
   */
  def getUsedVariableNames( variableNameMappings : Map[String, String] ) : Set[String] = { throw UnsupportedOperationException("PixelProgram:s are not yet implemented") }

  /**
   * Takes a map from variable names to their position in the variable array.
   */
  def generateCode( variableIndexes : Map[String, Int] ) : String = { throw UnsupportedOperationException("PixelProgram:s are not yet implemented") }


  def getSourceVariable( name : String, variableNameMappings : Map[String, String] ) : String = {

    // First check if there is some string setting with the same name, allowing configuration time mapping
    var s = getStringProperty( name, name )

    // Then map using parameter name mapping
    var s2 = variableNameMappings.get(s)
    if (s2 == null) s2 = s

    s2
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

  protected def setMappedVar( name : String, value : Float, variables : DataSample, variableNameMappings : Map[String, String] ) : Float = {

    var s = getSourceVariable(name, variableNameMappings)

    variables.setProperty( s, value )
  }
}