package org.flowpaint.ui.editors

import _root_.scala.xml.Node
import org.flowpaint.property.{Data, DataImpl}
import org.flowpaint.util.ConfigurationMetadata

/**
 * Metadata that can be used to specify and create instances of editors.
 *
 * @param initialSettings settings for the editor.
 *
 * @author Hans Haggstrom
 */
class EditorMetadata( editorType : Class[ _ <: Editor], initialSettings : Data )
        extends ConfigurationMetadata[Editor]( editorType, initialSettings )

