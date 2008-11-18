package org.flowpaint.util

/**
 *
 *
 * @author Hans Haggstrom
 */

object ColorUtils {
  def getRed(rgba: Int): Float = ((rgba >> 16) & 0xff) / 255f

  def getGreen(rgba: Int): Float = ((rgba >> 8) & 0xff) / 255f

  def getBlue(rgba: Int): Float = ((rgba >> 0) & 0xff) / 255f

  def getAlpha(rgba: Int): Float = ((rgba >> 24) & 0xff) / 255f

  def createRGBAColor(r: Float, g: Float, b: Float, a: Float): Int =
    (((255 * a).toInt & 0xFF) << 24) |
            (((255 * r).toInt & 0xFF) << 16) |
            (((255 * g).toInt & 0xFF) << 8) |
            (((255 * b).toInt & 0xFF) << 0)

  def mixRGBA(alpha: Float, color: Int, originalColor: Int): Int = {

    val r = ((originalColor >> 16) & 0xff) * (1f - alpha) +
            ((color >> 16) & 0xff) * alpha
    val g = ((originalColor >> 8) & 0xff) * (1f - alpha) +
            ((color >> 8) & 0xff) * alpha
    val b = ((originalColor >> 0) & 0xff) * (1f - alpha) +
            ((color >> 0) & 0xff) * alpha
    val a = ((originalColor >> 24) & 0xff) * (1f - alpha) +
            ((color >> 24) & 0xff) * alpha

    ((a.toInt & 0xFF) << 24) |
            ((r.toInt & 0xFF) << 16) |
            ((g.toInt & 0xFF) << 8) |
            ((b.toInt & 0xFF) << 0)

  }

  def mixRGBWithAlpha(topColor: Int, bottomColor: Int): Int = {

    val topAlpha = getAlpha(topColor)
    val bottomAlpha = getAlpha(bottomColor)

    // The remaining transparency can be calculated by multiplying the see-through value (inverse alpha)
    // of the two layers, the resulting see-through amount is how much background is visible anymore.
    // E.g. if each layer blocks half the visibility, only 25% visiblity is left, hence 75% opaqueness.
    val resultAlpha = 1f - (1f - topAlpha) * (1f - bottomAlpha)
    val a = (255 * resultAlpha).toInt

    val r = ((bottomColor >> 16) & 0xff) * (1f - topAlpha) +
            ((topColor >> 16) & 0xff) * topAlpha
    val g = ((bottomColor >> 8) & 0xff) * (1f - topAlpha) +
            ((topColor >> 8) & 0xff) * topAlpha
    val b = ((bottomColor >> 0) & 0xff) * (1f - topAlpha) +
            ((topColor >> 0) & 0xff) * topAlpha

    ((a.toInt & 0xFF) << 24) |
            ((r.toInt & 0xFF) << 16) |
            ((g.toInt & 0xFF) << 8) |
            ((b.toInt & 0xFF) << 0)

  }


}