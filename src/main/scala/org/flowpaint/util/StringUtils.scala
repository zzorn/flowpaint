package org.flowpaint.util

/**
 *
 *
 * @author Hans Haggstrom
 */

object StringUtils {


  /**
   * Adds zeroes in front of a non-negative integer until we get a string of the desired length.
   */
  def zeroPadInteger(number: Int, desiredLength: Int) : String = {
    if (number < 0) throw new IllegalArgumentException("Argument should not be negative")

    var numZeropad = number.toString
    while (numZeropad.length < desiredLength) {
      numZeropad = "0" + numZeropad;
    }
    return numZeropad
  }


}