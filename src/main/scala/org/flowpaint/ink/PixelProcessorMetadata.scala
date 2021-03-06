package org.flowpaint.ink

import scala.xml.Node
import org.flowpaint.property.{Data, DataImpl}
import org.flowpaint.util.ConfigurationMetadata



/**
 * Metadata that can be used to specify and create instances of pixel processors.
 * 
 * @author Hans Haggstrom
 */
class PixelProcessorMetadata( processorType : Class[ _ <: Ink], initialSettings : Data )
        extends ConfigurationMetadata[Ink]( processorType, initialSettings )

