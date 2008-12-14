package org.flowpaint.filters

import _root_.org.flowpaint.property.{DataImpl, Data}

/**
 * 
 * 
 * @author Hans Haggstrom
 */
// TODO: Add UI widgets
class PathProcessorMetadata( processorType : Class[ _ <: PathProcessor] ) {

  val settings = new DataImpl()

  def createPathProcessor() : PathProcessor = {
    val processor : PathProcessor = processorType.newInstance
    processor.init(settings)
    processor
  }


}