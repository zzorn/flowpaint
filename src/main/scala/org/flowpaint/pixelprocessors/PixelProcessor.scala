package org.flowpaint.pixelprocessors
/**
 * 
 * 
 * @author Hans Haggstrom
 */

abstract class PixelProcessor {

  def getUsedVariables() : Set[String]

  /**
   * Takes a map from variable names to their position in the variable array.
   */
  def generateCode( variableIndexes : Map[String, Int] ) : String

}