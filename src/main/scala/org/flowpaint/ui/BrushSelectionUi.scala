package org.flowpaint.ui

import _root_.scala.collection.jcl.ArrayList
import brush.Brush
import javax.swing.JPanel

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class BrushSelectionUi(brushes : ArrayList[Brush]) extends JPanel{

  setLayout( new java.awt.GridLayout( 0,1 ) )

  brushes.foreach( brush => add( new BrushPreview(brush) ) )
}