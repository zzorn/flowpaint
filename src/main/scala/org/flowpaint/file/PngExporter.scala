package org.flowpaint.file

import java.awt.image.BufferedImage
import java.io.{File, IOException}

/**
 * Exports the current visible area of a picture as a png.
 *
 * @author Hans Haggstrom
 */
object PngExporter {

  /**
   * Tries to do the export to the specified file, return null on success, otherwise an error message.
   */
  def exportPng( image : BufferedImage, file : File) : String = {

    // Write generated image to a file
    try {
        // Save as PNG
        javax.imageio.ImageIO.write(image, "png", file);
    }
    catch {
      case e : IOException => return "Problem saving '"+file+"': " + e.getMessage
    }

    return null
  }


}