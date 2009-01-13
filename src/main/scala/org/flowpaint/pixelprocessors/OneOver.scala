package org.flowpaint.pixelprocessors
import pixelprocessor.SingleFunctionPixelProcessor

/**
 * 
 *
 * @author Hans Haggstrom
 */

class OneOver extends SingleFunctionPixelProcessor( x => 1f / x, "1f / " )