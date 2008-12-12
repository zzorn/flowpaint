package org.flowpaint.filters
import property.Data

/**
 * 
 * 
 * @author Hans Haggstrom
 */
// TODO: Add UI widgets
class PathProcessorMetadata( processorType : Class[ _ <: PathProcessor] ) {

  val settings = new Data()

  def createPathProcessor() : PathProcessor = {
    val processor : PathProcessor = processorType.newInstance
    processor.init(settings)
    processor
  }


}