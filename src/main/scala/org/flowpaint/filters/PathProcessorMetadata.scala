package org.flowpaint.filters

import _root_.org.flowpaint.property.{DataImpl, Data}
import _root_.scala.xml.Node
import util.ConfigurationMetadata


/**
 * Metadata that can be used to specify and create instances of path processors.
 *
 * @author Hans Haggstrom
 */
class PathProcessorMetadata( processorType : Class[ _ <: PathProcessor], initialSettings : Data )
        extends ConfigurationMetadata[PathProcessor]( processorType, initialSettings )
