package org.flowpaint.ink

import property.{Data, DataImpl}
import util.ProcessorMetadata

/**
 * Metadata that can be used to specify and create instances of pixel processors.
 * 
 * @author Hans Haggstrom
 */
class PixelProcessorMetadata( processorType : Class[ _ <: Ink], initialSettings : Data )
        extends ProcessorMetadata[Ink]( processorType, initialSettings )

