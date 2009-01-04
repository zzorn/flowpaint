package org.flowpaint.ink

import _root_.scala.xml.Node
import property.{Data, DataImpl}
import util.ConfigurationMetadata



/**
 * Metadata that can be used to specify and create instances of pixel processors.
 * 
 * @author Hans Haggstrom
 */
class PixelProcessorMetadata( processorType : Class[ _ <: Ink], initialSettings : Data )
        extends ConfigurationMetadata[Ink]( processorType, initialSettings )

