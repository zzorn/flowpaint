package org.flowpaint.ui

import _root_.org.flowpaint.util.DataSample
import javax.swing.JPanel

/**
 * A trait for ui components that can be used to adjust parameters.
 *
 * @param editedData The data sample whose values should be changed when this parameter ui is tweaked by the user.
 *
 * @author Hans Haggstrom
 */
abstract class ParameterUi( editedData : DataSample ) extends JPanel {


  // TODO: Add various layout functionality here, maybe hints on what proportions and size this component
  // should have.

  // TODO: Also add drag and drop functionality (middle mouse button could be used),
  // to allow a user to reconfigure a panel with parameter controls.
  // This means that this component also should know it's host component.

  
}