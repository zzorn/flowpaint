package org.flowpaint.file

import java.awt.Image
import java.awt.image.{RenderedImage, BufferedImage}
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
  def exportPng( image : RenderedImage, file : File) : String = {

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