package org.flowpaint.ui.editors

import _root_.scala.xml.Node
import property.{Data, DataImpl}
import util.ConfigurationMetadata

/**
 * Metadata that can be used to specify and create instances of editors.
 *
 * @param initialSettings settings for the editor.
 *
 * @author Hans Haggstrom
 */
class EditorMetadata( editorType : Class[ _ <: Editor], initialSettings : Data )
        extends ConfigurationMetadata[Editor]( editorType, initialSettings )

