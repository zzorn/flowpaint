package org.flowpaint.actions

import java.io.File

/**
 * 
 *
 * @author Hans Haggstrom
 */

object QuickSave {

  def invokeAction() {

    val result = quickSave()

    if (result != null) FlowPaintUi.showMessage(result)
  }

  def quickSave() : String = {

    // Find a free filename
    val fileName = findFreeFileName( FlowPaint.APPLICATION_NAME+"-QuickSave-", ".png" )
    if (fileName == null) return "No free filename was found"

    // Get the currently visible image
    val buffer = FlowPaintController.surface.buffer
    if (buffer == null) return "Image not rendered yet"

    // Try to export
    val exportResult = file.PngExporter.exportPng( buffer, fileName )

    // Show message if we succeeded
    if (exportResult == null) FlowPaintUi.showMessage( "Saved current picture to '"+fileName.getAbsolutePath().toString+"'" )

    return exportResult
  }

  /**
   * Returns a non-existing filename based on the specified name, by adding numbers to it.
   * Return null if no filename was found after max number of tries.
   */
  def findFreeFileName( baseName : String, suffix : String ) : File = {

    for ( i <- 1 to 9999 ) {

      val name =  baseName + util.StringUtils.zeroPadInteger( i, 3 ) + suffix

      val file = new File( name )

      if (!file.exists) return file
    }

    return null
  }

}