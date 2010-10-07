package org.flowpaint.filters

import org.flowpaint.property.{DataImpl, Data}
import scala.xml.Node
import org.flowpaint.util.ConfigurationMetadata


/**
 * Metadata that can be used to specify and create instances of path processors.
 *
 * @author Hans Haggstrom
 */
class PathProcessorMetadata( processorType : Class[ _ <: PathProcessor], initialSettings : Data )
        extends ConfigurationMetadata[PathProcessor]( processorType, initialSettings )
