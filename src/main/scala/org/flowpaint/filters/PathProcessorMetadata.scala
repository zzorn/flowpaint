package org.flowpaint.filters

import _root_.org.flowpaint.property.{DataImpl, Data}
import _root_.scala.xml.Node
import util.ProcessorMetadata

/**
 * Deserialization
 */
// TODO: Can we move shared deserialization to the ProcessorMetadata? Maybe unify the pixel and path processor metadatas?
object PathProcessorMetadata {
    def fromXML(node : Node) : PathProcessorMetadata = {

        val processorTypeName = (node \ "@type").text
        val settings = Data.fromXML( node )

      // Get class based on class name
      // TODO: Maybe check that the type is one of the allowed ones? (whitelist), to increase security.
      val processorType = Class.forName(processorTypeName)

      if (! classOf[PathProcessor].isAssignableFrom( processorType ) ) throw new IllegalArgumentException( "Path processors should be of type PathProcessor, but a type of '"+processorType+"' was requested " )

      new PathProcessorMetadata( processorType.asInstanceOf[Class[PathProcessor]] , settings )
    }

}


/**
 * Metadata that can be used to specify and create instances of path processors.
 *
 * @author Hans Haggstrom
 */
class PathProcessorMetadata( processorType : Class[ _ <: PathProcessor], initialSettings : Data )
        extends ProcessorMetadata[PathProcessor]( processorType, initialSettings )
