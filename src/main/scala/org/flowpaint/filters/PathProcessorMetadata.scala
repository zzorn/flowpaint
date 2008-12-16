package org.flowpaint.filters

import _root_.org.flowpaint.property.{DataImpl, Data}
import util.ProcessorMetadata

/**
 * Metadata that can be used to specify and create instances of path processors.
 *
 * @author Hans Haggstrom
 */
class PathProcessorMetadata( processorType : Class[ _ <: PathProcessor], initialSettings : Data )
        extends ProcessorMetadata[PathProcessor]( processorType, initialSettings )
