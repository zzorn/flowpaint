package org.flowpaint.property

import _root_.org.flowpaint.util.DataSample
import brush.Brush
import javax.swing.JComponent

/**
 * Metadata for an editor that can be used to edit some properties in a Data object.
 *
 * @param title A title or caption to use for this editor in the UI.
 *
 * @author Hans Haggstrom
 */
abstract class DataEditor( val title :String ) {

  def createEditor( editedData : Data, brush : Brush ) : JComponent

}



