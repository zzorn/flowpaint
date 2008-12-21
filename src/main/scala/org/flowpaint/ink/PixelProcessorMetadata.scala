package org.flowpaint.ink

import _root_.scala.xml.Node
import property.{Data, DataImpl}
import util.ProcessorMetadata

/**
 * Deserialization
 */
// TODO: Can we move shared deserialization to the ProcessorMetadata? Maybe unify the pixel and path processor metadatas?
object PixelProcessorMetadata {
    def fromXML(node : Node) : PixelProcessorMetadata = {

        val processorTypeName = (node \ "@type").text
        val settings = Data.fromXML( node )

      // Get class based on class name
      // TODO: Maybe check that the type is one of the allowed ones? (whitelist), to increase security.
      val processorType  = Class.forName(processorTypeName)

      if (! classOf[Ink].isAssignableFrom( processorType ) ) throw new IllegalArgumentException( "Pixel processors should be of type Ink, but a type of '"+processorType+"' was requested " )

      new PixelProcessorMetadata( processorType.asInstanceOf[Class[Ink]] , settings )
    }

}


/**
 * Metadata that can be used to specify and create instances of pixel processors.
 * 
 * @author Hans Haggstrom
 */
class PixelProcessorMetadata( processorType : Class[ _ <: Ink], initialSettings : Data )
        extends ProcessorMetadata[Ink]( processorType, initialSettings )

